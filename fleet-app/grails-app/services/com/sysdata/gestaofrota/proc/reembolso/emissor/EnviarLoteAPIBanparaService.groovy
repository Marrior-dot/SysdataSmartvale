package com.sysdata.gestaofrota.proc.reembolso.emissor

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.http.RESTClientHelper
import com.sysdata.gestaofrota.http.ResponseData
import grails.converters.JSON
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

import java.text.SimpleDateFormat


@Transactional
class EnviarLoteAPIBanparaService implements ExecutableProcessing, TokenBanparaAPI {

    GrailsApplication grailsApplication

    private void aceitarLotePagamento(LotePagamento lotePagamento) {
        lotePagamento.status = StatusLotePagamento.ACEITO
        lotePagamento.pagamentos.each { PagamentoLote pgLt ->
            pgLt.status = StatusPagamentoLote.ACEITO
            pgLt.save(flush: true)
        }
    }

    private void rejeitarLotePagamento(LotePagamento lotePagamento, ResponseData responseData) {

        if (responseData.json) {

            StatusRetornoPagamento statusRetorno = StatusRetornoPagamento.findByCodigo(responseData.json.codErro.toString())
            if (!statusRetorno) {
                statusRetorno = new StatusRetornoPagamento(codigo: responseData.json.codErro.toString(), descricao: responseData.json.msgErro)
                statusRetorno.save(flush: true)
            } else if (statusRetorno && statusRetorno.descricao != responseData.json.msgErro ) {
                statusRetorno.descricao = responseData.json.msgErro
                statusRetorno.save(flush: true)
            }

            lotePagamento.statusRetorno = statusRetorno
        }

        lotePagamento.status = StatusLotePagamento.REJEITADO
        lotePagamento.save(flush: true)

        lotePagamento.pagamentos.each { PagamentoLote pgLt ->
            pgLt.status = StatusPagamentoLote.REJEITADO
            pgLt.save(flush: true)
        }

    }

    private void enviarMensagem(PagamentoLote pagamentoLote) {

        def json = [
            Operador: grailsApplication.config.projeto.reembolso.banpara.api.lote.operador,
            dataContabil: pagamentoLote.dataPrevista.format('yyyy-MM-dd'),
            CNPJOrgao: pagamentoLote.estabelecimento.cnpj,
            AgenciaContaOrgao: pagamentoLote.dadoBancario.agencia as int,
            ContaOrgao: ""
        ]

    }



    @Override
    def execute(Date date) {
        def pars = [sort: "dateCreated"]
        List<LotePagamento> lotePagtoList = LotePagamento.findAllByStatusEmissao(StatusEmissao.GERAR_ARQUIVO, pars)

        lotePagtoList.each { lote ->

            log.info "Preparando Lote de Pagamento $lote.id para envio a API Banpara..."

            lote.pagamentos.each { pg ->

                enviarMensagem(pg)
            }






            withToken { token ->
                MensagemIntegracao msgEnviaLote = new MensagemIntegracao()
                msgEnviaLote.with {
                    tipo = TipoMensagem.BANPARA_ENVIO_LOTEPAGAMENTO
                    corpo = (loteJson as JSON).toString(true)
                }
                msgEnviaLote.save(flush: true)

                def endpoint = grailsApplication.config.projeto.reembolso.banpara.api.lote.endpoint

                def header = [:]
                if (token)
                    header.Authorization = "Bearer $token"

                ResponseData responseData = RESTClientHelper.instance.postJSON(endpoint, loteJson, header)

                if (!responseData)
                    throw new RuntimeException("Sem resposta do servidor!")

                msgEnviaLote.codigoResposta = responseData.statusCode.toString()
                msgEnviaLote.resposta = responseData.body
                msgEnviaLote.save(flush: true)

                lote.statusEmissao = StatusEmissao.ARQUIVO_GERADO

                switch (responseData.statusCode) {

                    case 200:
                        aceitarLotePagamento(lote)
                        break

                    case 400:
                        rejeitarLotePagamento(lote, responseData)
                        break

                    case 500:
                        rejeitarLotePagamento(lote, responseData)
                        break

                    default:
                        log.error("Codigo Retorno (${responseData.statusCode}) não tratado")
                        break
                }
            }
        }
    }
}
