package com.sysdata.gestaofrota.proc.reembolso

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.CorteConvenio
import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.ItemFatura
import com.sysdata.gestaofrota.LancamentoConvenio
import com.sysdata.gestaofrota.LotePagamento
import com.sysdata.gestaofrota.LoteRecebimento
import com.sysdata.gestaofrota.PagamentoEstabelecimento
import com.sysdata.gestaofrota.PagamentoLote
import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.StatusCorte
import com.sysdata.gestaofrota.StatusLancamento
import com.sysdata.gestaofrota.StatusLotePagamento
import com.sysdata.gestaofrota.StatusReenvioPagamento
import com.sysdata.gestaofrota.TipoFatura
import com.sysdata.gestaofrota.TipoLancamento
import com.sysdata.gestaofrota.proc.agendamento.CalculoDiasUteis
import grails.gorm.transactions.Transactional

@Transactional
class FechamentoLotePagamentoService implements ExecutableProcessing, CalculoDiasUteis {

    @Override
    def execute(Date date) {
        log.info "Iniciando Fechamento de Lote de Repasses..."

        if (isDataUtil(date)) {
            List<LotePagamento> lotePagtoList = LotePagamento.findAllByStatus(StatusLotePagamento.ABERTO)

            if (!lotePagtoList.isEmpty()) {

                if (lotePagtoList.size() > 1)
                    throw new RuntimeException("Não pode haver mais de um Lote de Pagamento aberto!!!")

                LotePagamento lotePagamento = lotePagtoList[0]

                log.info "Lote Pagto #${lotePagamento.id}:"
                def pagIds = LotePagamento.withCriteria {
                                createAlias("cortes", "c")
                                createAlias("c.pagamentos", "p")
                                projections {
                                    property("p.id")
                                }
                                eq("id", lotePagamento.id)
                                order("p.estabelecimento")
                            }

                def ultPagIds = []

                // Checa se existem pagamentos recusados em lotes anteriores que estão marcados para entrar neste
                List<PagamentoEstabelecimento> pagtosReenvio = PagamentoEstabelecimento.withCriteria {
                    eq("statusReenvio", StatusReenvioPagamento.REENVIAR_PAGAMENTO)
                    order("estabelecimento")
                }

                if (pagtosReenvio) {
                    // Caso já exista um pagamento agendado normal para um EC, o pagamento recusado deve ser o próximo após o pagto agendado
                    if (pagIds) {
                        pagIds.each { pid ->
                            PagamentoEstabelecimento pagamento = PagamentoEstabelecimento.get(pid)
                            ultPagIds << pagamento.id
                            List<PagamentoEstabelecimento> lotesMesmoEC = pagtosReenvio.findAll { it.estabelecimento == pagamento.estabelecimento }
                            // Inclui na lista de pagamentos os que são do mesmo EC do pagamento agendado normal
                            if (lotesMesmoEC) {
                                ultPagIds << lotesMesmoEC*.pagamentos*.id
                                pagtosReenvio.removeAll(lotesMesmoEC)
                            }
                        }
                        // Se não existe na lista de pagamentos normais, inclui no final
                        if (! pagtosReenvio.isEmpty())
                            ultPagIds << pagtosReenvio*.pagamentos*.id

                    } else
                        ultPagIds = pagtosReenvio*.pagamentos*.id
                } else
                    ultPagIds = pagIds


                if (ultPagIds) {
                    def valorPagto = 0.0
                    PagamentoEstabelecimento pagamento
                    def pagtoList = []
                    ultPagIds.eachWithIndex { pid, idx ->
                        PagamentoEstabelecimento currPagto = PagamentoEstabelecimento.get(pid)
                        if(pagamento && pagamento.estabelecimento != currPagto.estabelecimento) {
                            criarLotePagamento(lotePagamento, pagamento, valorPagto, pagtoList, date)
                            valorPagto = 0.0
                            pagtoList.clear()
                        }
                        pagamento = currPagto
                        pagtoList << currPagto
                        valorPagto += pagamento.valor
                        if (idx + 1 == 50)
                            clearSession()
                    }
                    if (!pagamento.isAttached())
                        pagamento.attach()
                    if (!lotePagamento.isAttached())
                        lotePagamento.attach()
                    criarLotePagamento(lotePagamento, pagamento, valorPagto, pagtoList, date)

                    lotePagamento.dataEfetivacao = lotePagamento.pagamentos.max { it.dataPrevista }
                    lotePagamento.status = StatusLotePagamento.FECHADO
                    lotePagamento.save(flush: true)

                    // Marca PagamentoLote recusados para reenviar como incluídos no LotePagamento atual
                    if (pagtosReenvio) {
                        pagtosReenvio.each { pgLot ->
                            pgLot.statusReenvio = StatusReenvioPagamento.INCLUIDO_LOTE
                            pgLot.save(flush: true)
                        }
                    }
                } else
                    throw new RuntimeException("Sem pagamentos relacionados ao Lote #${lotePagamento.id}")

            } else
                log.warn "Não há Lote de Pagamento aberto para processar"

        } else
            log.warn "Não há Fechamento de Repasses em dias não úteis!"

        log.info "Fechamento de Lote de Pagamento finalizado"
    }

    private void criarLotePagamento(LotePagamento loteLiquidacao, PagamentoEstabelecimento pagamento, BigDecimal valorPagto,
                                    List<PagamentoEstabelecimento> pagtoList, Date dataOper) {

        log.debug "\tLote #${loteLiquidacao.id} pg #$pagamento.id vl:$valorPagto pgtos: $pagtoList"

        PagamentoLote pagtoLote = new PagamentoLote()
        pagtoLote.with {
            estabelecimento = pagamento.estabelecimento
            valor = valorPagto
            dataPrevista = pagamento.dataProgramada
            dadoBancario = pagamento.estabelecimento.dadoBancario
            pagamentos = pagtoList as Set
        }
        log.info "\t$pagtoLote"
        loteLiquidacao.addToPagamentos(pagtoLote)
        loteLiquidacao.save()
    }

}
