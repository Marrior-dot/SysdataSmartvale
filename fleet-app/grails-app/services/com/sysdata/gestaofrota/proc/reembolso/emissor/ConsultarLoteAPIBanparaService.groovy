package com.sysdata.gestaofrota.proc.reembolso.emissor

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.http.RESTClientHelper
import com.sysdata.gestaofrota.http.ResponseData
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class ConsultarLoteAPIBanparaService implements ExecutableProcessing, TokenBanparaAPI {

    GrailsApplication grailsApplication

    private void liquidarPagamento(PagamentoLote pgtoLote) {
        pgtoLote.status = StatusPagamentoLote.LIQUIDADO
        pgtoLote.save(flush: true)
        log.info "PG LT #${pgtoLote.id} liq"
        def pgIds = pgtoLote.pagamentos*.id
        pgIds.each { pid ->
            PagamentoEstabelecimento pg = PagamentoEstabelecimento.get(pid)
            pg.status = StatusPagamento.LIQUIDADO
            pg.save(flush: true)
            log.info "\tPG #${pg.id} liq"

            def lancIds = LancamentoEstabelecimento.lancamentoPagamento(pg).list()
            lancIds.eachWithIndex { lid, idx ->
                LancamentoEstabelecimento recebivel = LancamentoEstabelecimento.get(lid)
                recebivel.status = StatusLancamento.LIQUIDADO
                recebivel.save(flush: true)
                log.info "\t\tLC #${recebivel.id} liq"
                if ((idx + 1) % 50 == 0)
                    clearSession()
            }
        }
    }




    private void tratarRetorno(PagamentoLote pgtoLote, tefTed) {

        switch (tefTed.status) {
            case 1:
                log.info "PG #${pgtoLote.id} Pendente"
                break

            case 2:
                // Liquidado somente quando TEF - conta destino BANPARÁ
                if (pgtoLote.dadoBancario.banco.codigo == "37") {
                    log.info "Conta Banpara:"
                    liquidarPagamento(pgtoLote)
                } else
                    log.info "PG #${pgtoLote.id} Confirmado - (conta não Banpará)"
                
                break

            case [3, 4]:
                pgtoLote.status = StatusPagamentoLote.REJEITADO
                StatusRetornoPagamento statusRetorno = StatusRetornoPagamento.findByDescricao(tefTed.mensagem)
                if (! statusRetorno) {
                    Integer novoCodigo = StatusRetornoPagamento.findNextCodigo()
                    statusRetorno = new StatusRetornoPagamento(codigo: novoCodigo, descricao: tefTed.mensagem)
                    statusRetorno.save(flush: true)
                }
                pgtoLote.statusRetorno = statusRetorno
                pgtoLote.save(flush: true)
                log.info "PG #${pgtoLote.id} Rejeitado. Status: ${tefTed.status}"
                break

            default:
                throw new RuntimeException("Erro não mapeado: ${tefTed.status}!")

        }

    }

    private void tratarHttpOk(LotePagamento lote, ResponseData response) {

        if (response.json) {

            def totalPagamentos = lote.pagamentos.size()

            lote.pagamentos.each { pgLot ->

                def ted = response.json.ted.find { it.nsr == pgLot.id }
                if (ted)
                    tratarRetorno(pgLot, ted)

                def tef = response.json.tef.find { it.nsr == pgLot.id }
                if (tef)
                    tratarRetorno(pgLot, tef)

            }

            def totalRejeitados = lote.pagamentos.count { it.status == StatusPagamentoLote.REJEITADO }
            def totalLiquidados = lote.pagamentos.count { it.status == StatusPagamentoLote.LIQUIDADO }

            if (totalPagamentos == totalRejeitados)
                lote.status = StatusLotePagamento.REJEITADO
            else if (totalPagamentos == totalLiquidados)
                lote.status = StatusLotePagamento.LIQUIDADO
            else if (totalPagamentos == (totalLiquidados + totalRejeitados))
                lote.status = StatusLotePagamento.LIQUIDADO_PARCIALMENTE

            lote.save(flush: true)

        }

    }


    @Override
    def execute(Date date) {
        List<LotePagamento> lotePgtoList = LotePagamento.where {
                                                status == StatusLotePagamento.ACEITO
                                            }.list()

        if (!lotePgtoList.isEmpty()) {

            lotePgtoList.each { lote ->

                withToken { token ->

                    def query = [:]
                    query.Operador = grailsApplication.config.projeto.reembolso.banpara.api.lote.operador
                    query.dataContabil = lote.dateCreated.format('yyyy-MM-dd')
                    query.NSL = lote.id

                    MensagemIntegracao msgEnviaLote = new MensagemIntegracao()
                    msgEnviaLote.with {
                        tipo = TipoMensagem.BANPARA_CONSULTA_LOTEPAGAMENTO
                        corpo = query.toString()
                    }
                    msgEnviaLote.save(flush: true)

                    def endpoint = grailsApplication.config.projeto.reembolso.banpara.api.lote.endpoint

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
                        tratarHttpOk(lote, responseData)

                    else
                        log.error "Erro ao Consultar Lote #${lote.id}. HTTP Status: ${responseData.statusCode}"
                }
            }


        } else
            log.warn "Não há lotes a consultar"

    }
}
