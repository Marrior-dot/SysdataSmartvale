package com.sysdata.gestaofrota.proc.reembolso.emissor

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.LotePagamento
import com.sysdata.gestaofrota.MensagemIntegracao
import com.sysdata.gestaofrota.PagamentoLote
import com.sysdata.gestaofrota.StatusPagamentoLote
import com.sysdata.gestaofrota.StatusRetornoPagamento
import com.sysdata.gestaofrota.TipoMensagem
import com.sysdata.gestaofrota.http.RESTClientHelper
import com.sysdata.gestaofrota.http.ResponseData
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class ConsultarLoteDevolucaoAPIBanparaService implements ExecutableProcessing, TokenBanparaAPI {

    GrailsApplication grailsApplication

    private void tratarHttpOk(ResponseData response) {

        if (response.json) {

            response.json.each { lt ->

                LotePagamento lotePagamento = LotePagamento.get(lt.nsl as long)
                if (lotePagamento) {
                    log.info "LT #${lotePagamento.id} - Devolução"

                    lt.ted.each { pg ->

                        PagamentoLote pagtoLote = lotePagamento.pagamentos.find { it.id == pg.nsr as long}

                        if (pagtoLote) {
                            pagtoLote.status = StatusPagamentoLote.REJEITADO
                            StatusRetornoPagamento statusRetorno = StatusRetornoPagamento.findByDescricao(pg.motivoDevolucao)
                            if (! statusRetorno) {
                                Integer novoCodigo = StatusRetornoPagamento.findNextCodigo()
                                statusRetorno = new StatusRetornoPagamento(codigo: novoCodigo, descricao: pg.motivoDevolucao)
                                statusRetorno.save(flush: true)
                            }
                            pagtoLote.statusRetorno = statusRetorno
                            pagtoLote.save(flush: true)
                            log.info "\tPG #${pagtoLote.id} - ${pg.motivoDevolucao}"
                        } else
                            log.error "\tPag NSR: ${pg.nsr} não encontrado"
                    }

                } else
                    log.error "Lote NLS: ${lt.nsl} não encontrado"

            }

        }
    }


    @Override
    def execute(Date date) {

        withToken { token ->

            def query = [:]
            query.Operador = grailsApplication.config.projeto.reembolso.banpara.api.lote.operador

            MensagemIntegracao msgEnviaLote = new MensagemIntegracao()
            msgEnviaLote.with {
                tipo = TipoMensagem.BANPARA_CONSULTA_LOTE_DEVOLUCAO
                corpo = query.toString()
            }
            msgEnviaLote.save(flush: true)

            def endpoint = grailsApplication.config.projeto.reembolso.banpara.api.lote.endpoint + "/devolucao"

            def header = [:]
            if (token)
                header.Authorization = "Bearer $token"

            ResponseData responseData = RESTClientHelper.instance.get(endpoint, query, header)

            if (!responseData)
                throw new RuntimeException("Sem resposta do servidor!")

            msgEnviaLote.codigoResposta = responseData.statusCode.toString()
            msgEnviaLote.resposta = responseData.body
            msgEnviaLote.save(flush: true)


            if (responseData.statusCode == 200)
                tratarHttpOk(responseData)

            else
                log.error "Erro ao Consultar Lotes Devolvidos. HTTP Status: ${responseData.statusCode}"

        }
    }
}
