package com.sysdata.gestaofrota.proc.reembolso

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFactory
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento
import grails.gorm.transactions.Transactional

@Transactional
class CorteReembolsoEstabsService implements ExecutableProcessing {

    def grailsApplication

    @Override
    def execute(Date date) {

        def datasCorte = LancamentoEstabelecimento.withCriteria {
                            projections {
                                groupProperty("dataEfetivacao")
                            }
                            'in'("tipo", [TipoLancamento.REEMBOLSO])
                            eq("status", StatusLancamento.A_FATURAR)
                            le("dataEfetivacao", date)
                            order("dataEfetivacao")
                        }

        if (datasCorte) {
            CorteEstabelecimento corteEstab = new CorteEstabelecimento()
            corteEstab.with {
                tipoCorte = TipoCorteEstabelecimento.REEMBOLSO
                status = StatusCorte.ABERTO
                dataPrevista = date
                dataFechamento = date
                dataCobranca = date + 1
            }
            corteEstab.save(flush: true)
            log.info "Corte EC #$corteEstab.id criado"
            corteEstab.datasCortadas = []

            LotePagamento loteAberto = LotePagamento.aberto
            if (!loteAberto) {
                loteAberto = new LotePagamento()
                loteAberto.save()
            }
            loteAberto.addToCortes(corteEstab)
            loteAberto.save()

            datasCorte.each { dt ->

                corteEstab.datasCortadas << dt
                log.info "Cortando Data ${dt.format('dd/MM/yy')} ..."
                def contasIds = LancamentoEstabelecimento.withCriteria {
                                        projections {
                                            groupProperty("conta.id")
                                        }
                                        eq("tipoLancamento", TipoLancamento.REEMBOLSO)
                                        eq("statusLancamento", StatusLancamento.A_FATURAR)
                                        eq("dataEfetivacao", dt)
                                }
                if (contasIds) {
                    contasIds.eachWithIndex { cid, i ->
                        Conta contaEstab = Conta.get(cid)
                        cortarConta(corteEstab, contaEstab, dt)
                        if ((i + 1) % 50 == 0)
                            clearSession()
                    }
                } else
                    log.warn "Não há contas de estabelecimento para corte!"
            }
            corteEstab.status = StatusCorte.FECHADO
            corteEstab.save(flush: true)

        } else
            log.info "Não há Corte de Reembolso para esta data ${date.format('dd/MM/yy')}"
    }

    private def cortarConta(Corte corte, Conta conta, Date dataRef) {

        def global = [:]

        PostoCombustivel estabelecimento = conta.participante

        log.info "\tEC: $estabelecimento"
        def List<LancamentoEstabelecimento> parcelaList = LancamentoEstabelecimento.withCriteria {
                                                            eq("tipoLancamento", TipoLancamento.REEMBOLSO)
                                                            eq("statusLancamento", StatusLancamento.A_FATURAR)
                                                            eq("dataEfetivacao", dataRef)
                                                            eq("conta", conta)
                                                        }

        def totalTransacoes = parcelaList.sum { it.valor }
        def totalCobrancas = calcularTotalCobrancas(estabelecimento, totalTransacoes, global)

        if (totalTransacoes - totalCobrancas > 0.0) {
            def totalLiquido = 0
            PagamentoEstabelecimento pagamento = new PagamentoEstabelecimento()
            pagamento.with {
                dataProgramada = dataRef
                estabelecimento = conta.participante
            }
            pagamento.corte = corte
            pagamento.save()
            parcelaList.each { lc ->
                totalLiquido += lc.valor
                lc.status = StatusLancamento.FATURADO
                lc.pagamento = pagamento
                lc.save()
                log.debug "\t\tLC #${lc.id} - vl.liq:${Util.formatCurrency(lc.valor)}"
            }
            pagamento.valor = totalLiquido

            gerarLancamentosExtensoes(global)

            pagamento.save(flush: true)
            corte.save(flush: true)
            log.info "PG #${pagamento.id} gerado - (total: ${Util.formatCurrency(pagamento.valor)})"

        } else {


            parcelaList.each {

            }

        }

    }

    def gerarLancamentosExtensoes(global) {

        global.extensoes.each { e ->
            ExtensaoFaturamento ext = ExtensaoFactory.getInstance(e.handler)
            ext.gerarLancamento(global)
        }

    }

    private def calcularTotalCobrancas(PostoCombustivel postoCombustivel, totalParcelas, global) {

        global.estabelecimento = postoCombustivel

        grailsApplication.config.project.faturamento.estabelecimento.extensoes.each { e ->
            ExtensaoFaturamento ext = ExtensaoFactory.getInstance(e)
            ext.calcularValor(global)
        }

        return global.extensoes.sum { it.valor }


    }

    private void processarExtensoes(PagamentoEstabelecimento pagamento, Conta contaEstab) {


    }


}
