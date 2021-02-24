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

        def rhs = [:]

        def datasCorte = LancamentoEstabelecimento.withCriteria {
                            projections {
                                groupProperty("dataEfetivacao")
                            }
                            'in'("tipo", [TipoLancamento.REEMBOLSO])
                            eq("status", StatusLancamento.A_FATURAR)
                            le("dataEfetivacao", date + 1)
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
                    log.warn "Não há contas de estabelecimento para corte!"
            }
            corteEstab.status = StatusCorte.FECHADO
            corteEstab.save(flush: true)

            cortarConvenio(date, rhs)

        } else
            log.info "Não há Corte de Reembolso para esta data ${date.format('dd/MM/yy')}"
    }

    private def cortarConvenio(Date dataOper, Map rhs) {
        // Gera Recebimentos por RH e os vincula ao Lote Recebimento aberto

        log.info "Cortando RHs para recebimento ..."
        CorteConvenio corteConvenio = new CorteConvenio()
        corteConvenio.with {
            status = StatusCorte.FECHADO
            dataPrevista = dataOper
            dataFechamento = dataOper
            dataCobranca = dataOper + 1
        }
        corteConvenio.save(flush: true)
        log.info "Corte Convênio #${corteConvenio.id} criado"

        rhs.each { k, v ->
            RecebimentoConvenio recebimentoConvenio = new RecebimentoConvenio()
            recebimentoConvenio.with {
                dataProgramada = dataOper + 1
                rh = Rh.get(k)
                corte = corteConvenio
                valor = v
            }
            recebimentoConvenio.save(flush: true)
            log.info "Rebecimento #${recebimentoConvenio.id} criado"
        }

        // Vincula Corte Convênio a Lote de Recebimento
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

        // Fatura somente se o saldo da diferença entre os lançamentos e as cobranças for positiva
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
                if (!rhs.containsKey(rh.id))
                    rhs[rh.id] = lc.valor
                else
                    rhs[rh.id] += lc.valor
            }
            pagamento.valor = totalLiquido
            pagamento.valorBruto = totalTransacoes
            pagamento.taxaAdm = estab.taxaReembolso

            //gerarLancamentosExtensoes(global)

            pagamento.save(flush: true)
            corte.save(flush: true)
            log.info "\tPG #${pagamento.id} gerado - (total: ${Util.formatCurrency(pagamento.valor)})"

            // Caso contrário, empurra os lançamentos para a próxima data possível de reembolso, conforme calendário
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

    private void processarExtensoes(PagamentoEstabelecimento pagamento, Conta contaEstab) {


    }


}
