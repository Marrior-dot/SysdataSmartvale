package com.sysdata.gestaofrota.proc.reembolso

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.agendamento.CalculoDiasUteis
import grails.gorm.transactions.Transactional

@Transactional
class CorteReembolsoEstabsService implements ExecutableProcessing, CalculoDiasUteis {

    def grailsApplication

    @Override
    def execute(Date date) {

        log.info "Iniciando Corte de ECs..."

        if (isDataUtil(date)) {
            def rhs = [:]

            def dateRef = dataUtil(date + 1)

            def datasCorte = LancamentoEstabelecimento.withCriteria {
                projections {
                    groupProperty("dataEfetivacao")
                }
                'in'("tipo", [TipoLancamento.REEMBOLSO])
                eq("status", StatusLancamento.A_FATURAR)
                le("dataEfetivacao", dateRef)
                order("dataEfetivacao")
            }

            if (datasCorte) {
                CorteEstabelecimento corteEstab = new CorteEstabelecimento()
                corteEstab.with {
                    tipoCorte = TipoCorteEstabelecimento.REEMBOLSO
                    status = StatusCorte.ABERTO
                    dataPrevista = date
                    dataFechamento = date
                    dataCobranca = dateRef
                }
                corteEstab.save(flush: true)
                log.info "Corte EC #$corteEstab.id criado"
                corteEstab.datasCortadas = []

                LotePagamento loteAberto = LotePagamento.aberto
                if (!loteAberto) {
                    loteAberto = new LotePagamento(dataEfetivacao: corteEstab.dataCobranca)
                    loteAberto.save(flush: true)
                }
                loteAberto.addToCortes(corteEstab)
                loteAberto.save(flush: true)

                datasCorte.each { dt ->

                    corteEstab.datasCortadas << dt
                    log.info "Cortando Data ${dt.format('dd/MM/yy')} ..."
                    def contasIds = LancamentoEstabelecimento.withCriteria {
                        projections {
                            groupProperty("conta.id")
                        }
                        eq("tipo", TipoLancamento.REEMBOLSO)
                        eq("status", StatusLancamento.A_FATURAR)
                        eq("dataEfetivacao", dt)
                    }
                    if (contasIds) {
                        contasIds.eachWithIndex { cid, i ->
                            Conta contaEstab = Conta.get(cid)
                            cortarConta(corteEstab, contaEstab, dt, rhs)
                            if ((i + 1) % 50 == 0)
                                clearSession()
                        }
                    } else
                        log.warn "N??o h?? contas de estabelecimento para corte!"
                }
                corteEstab.status = StatusCorte.FECHADO
                corteEstab.save(flush: true)

                cortarConvenio(date, rhs)

            } else
                log.info "N??o h?? Corte de Reembolso para esta data ${date.format('dd/MM/yy')}"
        } else
            log.warn "N??o h?? Corte de ECs em dias n??o ??teis!"

        log.info "Corte de ECs finalizado"
    }

    private def cortarConvenio(Date dataOper, Map rhs) {
        // Gera Recebimentos por RH e os vincula ao Lote Recebimento aberto



        log.info "Cortando RHs para recebimento ..."
        CorteConvenio corteConvenio = new CorteConvenio()
        corteConvenio.with {
            status = StatusCorte.FECHADO
            dataPrevista = dataOper
            dataFechamento = dataOper
            dataCobranca = dataUtil(dataOper)
        }
        corteConvenio.save(flush: true)
        log.info "Corte Conv??nio #${corteConvenio.id} criado"

        rhs.each { rhId, valores ->
            RecebimentoConvenio recebimentoConvenio = new RecebimentoConvenio()
            recebimentoConvenio.with {
                dataProgramada = dataUtil(dataOper)
                rh = Rh.get(rhId)
                corte = corteConvenio
                valor = valores.liquido
                valorBruto = valores.bruto
                valorTaxaAdm = valores.comissao
            }
            recebimentoConvenio.save(flush: true)
            log.info "Receb #${recebimentoConvenio.id} criado - (bru: ${recebimentoConvenio.valorBruto} liq: ${recebimentoConvenio.valor} com: ${recebimentoConvenio.valorTaxaAdm})"
        }

        // Vincula Corte Conv??nio a Lote de Recebimento
        LoteRecebimento loteAberto = LoteRecebimento.aberto
        loteAberto.addToCortes(corteConvenio)
        loteAberto.save(flush: true)

    }


    private def cortarConta(Corte corte, Conta conta, Date dataRef, Map rhs) {
        def global = [:]
        PostoCombustivel estab = conta.participante
        log.info "\t$estab"
        def List<LancamentoEstabelecimento> parcelaList = LancamentoEstabelecimento.withCriteria {
                                                                eq("tipo", TipoLancamento.REEMBOLSO)
                                                                eq("status", StatusLancamento.A_FATURAR)
                                                                eq("dataEfetivacao", dataRef)
                                                                eq("conta", conta)
                                                            }
        def totalLiquidoEstab = parcelaList.sum { it.valor }
        def totalCobrancas = calcularTotalCobrancas(estab, totalLiquidoEstab, global)
        def totalTransacoes = parcelaList.sum { it.transacao.valor }

        // Fatura somente se o saldo da diferen??a entre os lan??amentos e as cobran??as for positiva
        if (totalLiquidoEstab - totalCobrancas > 0.0) {
            def totalLiquido = 0
            PagamentoEstabelecimento pagamento = new PagamentoEstabelecimento()
            pagamento.with {
                dataProgramada = dataRef
                estabelecimento = estab
            }
            pagamento.corte = corte
            pagamento.save(flush: true)

            parcelaList.each { lc ->
                totalLiquido += lc.valor
                lc.status = StatusLancamento.FATURADO
                lc.pagamento = pagamento
                lc.save()
                log.debug "\t\tLC #${lc.id} - vl.liq:${Util.formatCurrency(lc.valor)}"
                // Acumula valor reembolso por RH p/ gerar Lote Recebimento
                Rh rh = lc.transacao.cartao.portador.unidade.rh
                if (! rhs.containsKey(rh.id))
                    rhs[rh.id] = [liquido: lc.valor, bruto: lc.transacao.valor, comissao: lc.transacao.valor - lc.valor]
                else {
                    rhs[rh.id].liquido += lc.valor
                    rhs[rh.id].bruto += lc.transacao.valor
                    rhs[rh.id].comissao += (lc.transacao.valor - lc.valor)
                }
            }
            pagamento.valor = totalLiquido
            pagamento.valorBruto = totalTransacoes
            pagamento.taxaAdm = estab.taxaReembolso


            pagamento.save(flush: true)
            corte.save(flush: true)
            log.info "\tPG #${pagamento.id} gerado - (total: ${Util.formatCurrency(pagamento.valor)})"

            // Caso contr??rio, empurra os lan??amentos para a pr??xima data poss??vel de reembolso, conforme calend??rio
        } else {


            parcelaList.each {

            }

        }

    }

    private def calcularTotalCobrancas(PostoCombustivel postoCombustivel, totalParcelas, global) {

/*
        global.estabelecimento = postoCombustivel

        grailsApplication.config.project.faturamento.estabelecimento.extensoes.each { e ->
            ExtensaoFaturamento ext = ExtensaoFactory.getInstance(e)
            ext.calcularValor(global)
        }

        return global.extensoes.sum { it.valor }
*/

        return 0.0

    }




/*

    def gerarLancamentosExtensoes(global) {

        global.extensoes.each { e ->
            ExtensaoFaturamento ext = ExtensaoFactory.getInstance(e.handler)
            ext.gerarLancamento(global)
        }

    }


    private void processarExtensoes(PagamentoEstabelecimento pagamento, Conta contaEstab) {


    }
*/


}
