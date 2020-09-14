package com.sysdata.gestaofrota.proc.reembolso

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import grails.gorm.transactions.Transactional

@Transactional
class CorteReembolsoEstabsService implements ExecutableProcessing {

    @Override
    def execute(Date date) {

        def datasCorte = LancamentoEstabelecimento.withCriteria {
                            projections {
                                groupProperty("dataEfetivacao")
                            }
                            'in'("tipo", [TipoLancamento.REEMBOLSO])
                            eq("status", StatusLancamento.A_PAGAR)
                            le("dataEfetivacao", date)
                            order("dataEfetivacao")
                        }

        if (datasCorte) {
            CorteEstabelecimento corteEstab = new CorteEstabelecimento()
            corteEstab.save(flush: true)
            log.info "Corte EC #$corteEstab.id criado"
            corteEstab.datasCortadas = []
            datasCorte.each { dt ->

                corteEstab.datasCortadas << dt
                log.info "Cortando Data ${dt.format('dd/MM/yy')} ..."
                def contasIds = LancamentoEstabelecimento.withCriteria {
                                        projections {
                                            groupProperty("conta.id")
                                        }
                                        eq("tipoLancamento", TipoLancamento.REEMBOLSO)
                                        eq("statusLancamento", StatusLancamento.A_EFETIVAR)
                                        eq("dataEfetivacao", dt)
                                }
                if (contasIds) {
                    contasIds.eachWithIndex { cid, i ->
                        Conta contaEstab = Conta.get(cid)
                        cortarConta(corte, contaEstab, dt)
                        if ((i + 1) % 50 == 0)
                            clearSession()
                    }
                } else
                    log.warn "Não há contas de estabelecimento para corte!"
            }
            corteEstab.status = StatusCorte.EXECUTADO
            corteEstab.save(flush: true)

        } else
            log.info "Não há Corte de Reembolso para esta data ${date.format('dd/MM/yy')}"
    }

    private def cortarConta(Corte corte, Conta conta, Date dataRef) {
        log.info "\tEC: $conta.participante"
        def List<LancamentoEstabelecimento> lctoList = LancamentoEstabelecimento.withCriteria {
                                                            eq("tipoLancamento", TipoLancamento.REEMBOLSO)
                                                            eq("statusLancamento", StatusLancamento.A_EFETIVAR)
                                                            eq("dataEfetivacao", dataRef)
                                                            eq("conta", conta)
                                                        }
        def totalLiquido = 0
        PagamentoEstabelecimento pagamento = new PagamentoEstabelecimento()
        pagamento.with {
            dataProgramada = dataRef
            estabelecimento = conta.participante
        }
        pagamento.corte = corte
        pagamento.save()
        lctoList.each { lc ->
            totalLiquido += lc.valor
            lc.status = StatusLancamento.FATURADO
            lc.pagamento = pagamento
            lc.save()
            log.debug "\t\tLC #${lc.id} - vl.liq:${Util.formatCurrency(lc.valor)}"
        }
        pagamento.valor = totalLiquido
        pagamento.save(flush: true)
        corte.save(flush: true)
        log.info "PG #${pagamento.id} gerado - (total: ${Util.formatCurrency(pagamento.valor)})"
    }


}
