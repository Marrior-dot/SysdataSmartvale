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

    private void aceitarLoteRecebimento(LoteRecebimento loteRecebimento) {
        loteRecebimento.status = StatusLotePagamento.ACEITO

        loteRecebimento.recebimentos.each { RecebimentoLote recLt ->
            recLt.status = StatusPagamentoLote.ACEITO
            recLt.save(flush: true)
        }
    }

    private void rejeitarLoteRecebimento(LoteRecebimento loteRecebimento, ResponseData responseData) {

        if (responseData.json) {

            StatusRetornoPagamento statusRetorno = StatusRetornoPagamento.findByCodigo(responseData.json.codErro.toString())
            if (!statusRetorno) {
                statusRetorno = new StatusRetornoPagamento(codigo: responseData.json.codErro.toString(), descricao: responseData.json.msgErro)
                statusRetorno.save(flush: true)
            } else if (statusRetorno && statusRetorno.descricao != responseData.json.msgErro ) {
                statusRetorno.descricao = responseData.json.msgErro
                statusRetorno.save(flush: true)
            }

            loteRecebimento.statusRetorno = statusRetorno
        }

        loteRecebimento.status = StatusLotePagamento.REJEITADO
        loteRecebimento.save(flush: true)

        loteRecebimento.recebimentos.each { RecebimentoLote recLt ->
            recLt.status = StatusPagamentoLote.REJEITADO
            recLt.save(flush: true)
        }

    }



    @Override
    def execute(Date date) {

        def pars = [sort: "dateCreated"]
        List<LoteRecebimento> loteRecebList = LoteRecebimento.findAllByStatusEmissao(StatusEmissao.GERAR_ARQUIVO, pars)

        if (loteRecebList) {

            loteRecebList.each { lote ->

                log.info "Preparando Lote de Recebimento $lote.id para envio a API Banpara..."

                def loteJson = [:]

                loteJson.Operador = grailsApplication.config.projeto.reembolso.banpara.api.lote.operador
                loteJson.dataContabil = lote.dateCreated.format('yyyy-MM-dd')
                loteJson.NSL = lote.id

                def tefList = lote.recebimentos.collect { receb ->
                    [
                            NSR: receb.id,
                            AgenciaContaOrig: receb.domicilioBancario.agencia,
                            ContaOrig: receb.domicilioBancario.conta,
                            ProdutoDest: 1,
                            AgenciaContaDest: grailsApplication.config.projeto.reembolso.banpara.contaDebito.agencia,
                            ContaDest: grailsApplication.config.projeto.reembolso.banpara.contaDebito.conta,
                            Valor: receb.valor
                    ]
                }

                loteJson.TEF = tefList

                withToken { token ->
                    MensagemIntegracao msgEnviaLote = new MensagemIntegracao()
                    msgEnviaLote.with {
                        tipo = TipoMensagem.BANPARA_ENVIO_LOTERECEBIMENTO
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
                            aceitarLoteRecebimento(lote)
                            break

                        case 400:
                            rejeitarLoteRecebimento(lote, responseData)
                            break

                        case 500:
                            rejeitarLoteRecebimento(lote, responseData)
                            break

                        default:
                            log.error("Codigo Retorno (${responseData.statusCode}) não tratado")
                            break
                    }
                }

            }


        } else
            log.warn "Não há Lote Recebimento para enviar"

    }
}
