package com.sysdata.gestaofrota.proc.reembolso.emissor

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.http.RESTClientHelper
import com.sysdata.gestaofrota.http.ResponseData
import grails.converters.JSON
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class EnviarLoteRecebimentoAPIBanparaService implements ExecutableProcessing, TokenBanparaAPI {

    GrailsApplication grailsApplication

    private void aceitarLoteRecebimento(RecebimentoLote recebimentoLote) {
        recebimentoLote.status = StatusPagamentoLote.ACEITO
        recebimentoLote.save(flush: true)
    }

    private void rejeitarLoteRecebimento(RecebimentoLote recebimentoLote, ResponseData responseData) {
        if (responseData.json) {
            StatusRetornoPagamento statusRetorno = StatusRetornoPagamento.findByCodigo(responseData.json.codigo.toString())
            if (!statusRetorno) {
                statusRetorno = new StatusRetornoPagamento(codigo: responseData.json.codigo.toString(), descricao: responseData.json.mensagem)
                statusRetorno.save(flush: true)
            } else if (statusRetorno && statusRetorno.descricao != responseData.json.mensagem ) {
                statusRetorno.descricao = responseData.json.mensagem
                statusRetorno.save(flush: true)
            }
            recebimentoLote.statusRetorno = statusRetorno
            recebimentoLote.status = StatusPagamentoLote.REJEITADO
            recebimentoLote.save(flush: true)
        } else
            log.error "Response de Rejeiçao sem detalhamento"

    }

    private void enviarMensagem(RecebimentoLote recebimentoLote) {

        def msgJson = [
            Operador: grailsApplication.config.projeto.reembolso.banpara.api.loteRecebimento.operador,
            DataContabil: recebimentoLote.dataPrevista.format('yyyy-MM-dd'),
            CNPJOrgao: recebimentoLote.convenio.documentoSemMascara as long,
            AgenciaOrgao: recebimentoLote.convenio.dadoBancario.agencia as int,
            ContaOrgao: recebimentoLote.convenio.dadoBancario.contaSemMascara as int,
            ValorDebito: recebimentoLote.valorBruto,
            AgenciaDestino: grailsApplication.config.projeto.reembolso.banpara.contaDebito.agencia as int,
            ContaDestino: grailsApplication.config.projeto.reembolso.banpara.contaDebito.conta as int,
            ValorCredito: recebimentoLote.valor,
            ValorComissao: recebimentoLote.valorTaxaAdm
        ]


        withToken { token ->

            if (grailsApplication.config.projeto.reembolso.banpara.api.jksFile2) {
                System.setProperty("javax.net.ssl.trustStore", grailsApplication.config.projeto.reembolso.banpara.api.jksFile2)
                System.setProperty("javax.net.ssl.trustStorePassword", grailsApplication.config.projeto.reembolso.banpara.api.password2)
            }

            MensagemIntegracao msgEnviaLote = new MensagemIntegracao()
            msgEnviaLote.with {
                tipo = TipoMensagem.BANPARA_ENVIO_LOTERECEBIMENTO
                corpo = (msgJson as JSON).toString(true)
            }
            msgEnviaLote.save(flush: true)

            def endpoint = grailsApplication.config.projeto.reembolso.banpara.api.loteRecebimento.endpoint

            def header = [:]
            if (token)
                header.Authorization = "Bearer $token"

            ResponseData responseData = RESTClientHelper.instance.postJSON(endpoint, msgJson, header)

            if (!responseData)
                throw new RuntimeException("Sem resposta do servidor!")

            msgEnviaLote.codigoResposta = responseData.statusCode.toString()
            msgEnviaLote.resposta = responseData.body
            msgEnviaLote.save(flush: true)

            switch (responseData.statusCode) {

                case 200:
                    aceitarLoteRecebimento(recebimentoLote)
                    break
                case 400:
                    rejeitarLoteRecebimento(recebimentoLote, responseData)
                    break
                case 500:
                    rejeitarLoteRecebimento(recebimentoLote, responseData)
                    break
                default:
                    log.error("Codigo Retorno (${responseData.statusCode}) não tratado")
                    break
            }
        }


    }

    @Override
    def execute(Date date) {

        def pars = [sort: "dateCreated"]
        List<LoteRecebimento> loteRecebList = LoteRecebimento.findAllByStatusEmissao(StatusEmissao.GERAR_ARQUIVO, pars)
        if (loteRecebList) {
            loteRecebList.each { lote ->
                log.info "Preparando Lote de Recebimento $lote.id para envio a API Banpara..."
                lote.recebimentos.each { receb ->
                    enviarMensagem(receb)
                }
                lote.statusEmissao = StatusEmissao.ARQUIVO_GERADO
                lote.save(flush: true)
            }
        } else
            log.warn "Não há Lote Recebimento para enviar"

    }
}
