package com.sysdata.gestaofrota.proc.faturamento

import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.ReferenceDateProcessing
import grails.gorm.transactions.Transactional

@Transactional
class CorteService {

    PortadorCorteService portadorCorteService

    /**
     *  Recupera todas as contas de portadores que possuem lançamentos a faturar no ciclo atual
     *
     */

    def faturar(Corte corte, Date dataCorte) {

        log.info "Faturando Corte $corte ..."

        def contasId = LancamentoPortador.withCriteria {
                            projections {
                                groupProperty("conta.id")
                            }
                            eq("statusFaturamento", StatusFaturamento.NAO_FATURADO)
                            conta {
                                portador {
                                    unidade {
                                        fechamentos {
                                            eq("id", corte.fechamento.id)
                                        }
                                    }
                                }
                            }
                            order("conta.id")
                        }

        if (contasId.isEmpty())
            log.info "Não há contas a faturar para este corte"
        else {
            corte.dataFechamento = dataCorte

            log.info "Total de Contas a Faturar: ${contasId.size()}"
            Conta contaRh = corte.fechamento.programa.conta
            Fatura fatRh = new Fatura()
            fatRh.with {
                conta = contaRh
                data = dataCorte
                dataVencimento = corte.dataCobranca
                status = StatusFatura.ABERTA
            }
            fatRh.corte = corte
            tratarAtraso(fatRh, dataCorte)
            def itensFatRh = [:]
            contasId.each {
                Conta conta = Conta.get(it)
                Fatura fatPort = portadorCorteService.faturar(conta.portador, corte, dataCorte)
                fatPort.itens.each { itf ->
                    if (!itensFatRh.containsKey(itf.lancamento.tipo))
                        itensFatRh[itf.lancamento.tipo] = 0.0
                    itensFatRh[itf.lancamento.tipo] += itf.valor
                }
            }
            //Gera lançamentos e itens da fatura Convênio
            itensFatRh.each { k, v ->
                LancamentoConvenio lcto = new LancamentoConvenio()
                lcto.with {
                    conta = contaRh
                    tipo = k
                    dataEfetivacao = dataCorte
                    valor = v
                    status = StatusLancamento.EFETIVADO
                    statusFaturamento = StatusFaturamento.FATURADO
                    corte = fatRh.corte
                }
                lcto.save()
                ItemFatura itemFatura = new ItemFatura()
                itemFatura.with {
                    data = lcto.dataEfetivacao
                    descricao = lcto.tipo.nome
                    valor = lcto.valor
                    lancamento = lcto
                    saldo = lcto.valor
                }
                fatRh.addToItens itemFatura
                fatRh.save()
            }

            //Gera boleto
            //fatRh.gerarBoleto()

            //Fecha última fatura e transfere saldo

            Corte proxCorte = fechar(dataCorte)

            //Gerar próximo corte
            def totalFatura = fatRh.valorTotal

            LancamentoConvenio fechamento = new LancamentoConvenio()
            fechamento.with {
                conta = fatRh.conta
                valor = -totalFatura
                tipo = TipoLancamento.FECHAMENTO_FATURA
                dataEfetivacao = dataCorte
                corte = fatRh.corte
                statusFaturamento = StatusFaturamento.FATURADO
            }
            fechamento.save()
            LancamentoConvenio saldoAnterior = new LancamentoConvenio()
            saldoAnterior.with {
                conta = fatRh.conta
                valor = totalFatura
                tipo = TipoLancamento.SALDO_ANTERIOR
                dataEfetivacao = dataCorte
                corte = proxCorte
                statusFaturamento = StatusFaturamento.NAO_FATURADO
            }
            saldoAnterior.save()
        }
        corte.save()
    }


    private void tratarAtraso(Fatura fatRh, Date dataProc) {
        Conta contaRh = fatRh.conta
        Rh rh = contaRh.participante
        def taxMulta = rh.multaAtraso
        def taxMora = rh.jurosProRata

        def pgtos = fatRh.itens.findAll { it.lancamento.tipo == TipoLancamento.PAGAMENTO }.sort { it.data }
        if (pgtos) {

            def saldoDevedor = contaRh.ultimaFatura.valorTotal
            def dataCtrl = dataProc
            def multa = 0.0
            def mora = 0.0

            pgtos.each { pg ->
                def delta = pg.data - dataCtrl
                if (delta > 0 && saldoDevedor > 0) {
                    if (!multa) multa = (saldoDevedor * taxMulta / 100).round(2)
                    mora += (saldoDevedor * (taxMora / delta) / 100).round(2)
                }
                saldoDevedor += pg.valor
                if (pg.data > dataProc)
                    dataCtrl = pg.data
            }

            if (multa > 0) {
                LancamentoConvenio lcnMulta = new LancamentoConvenio()
                lcnMulta.with {
                    conta = contaRh
                    tipo = TipoLancamento.MULTA
                    dataEfetivacao = dataProc
                    valor = multa
                    status = StatusLancamento.EFETIVADO
                    statusFaturamento = StatusFaturamento.FATURADO
                }
                lcnMulta.save()
                ItemFatura itemMulta = new ItemFatura()
                itemMulta.with {
                    data = lcnMulta.dataEfetivacao
                    descricao = lcnMulta.tipo.nome
                    valor = lcnMulta.valor
                    lancamento = lcnMulta
                }
                fatRh.addToItens itemMulta
                fatRh.save()
            }
            if (mora > 0) {
                LancamentoConvenio lcnMora = new LancamentoConvenio()
                lcnMora.with {
                    conta = contaRh
                    tipo = TipoLancamento.MORA
                    dataEfetivacao = dataProc
                    valor = multa
                    status = StatusLancamento.EFETIVADO
                    statusFaturamento = StatusFaturamento.FATURADO
                }
                lcnMora.save()
                ItemFatura itemMora = new ItemFatura()
                itemMora.with {
                    data = lcnMora.dataEfetivacao
                    descricao = lcnMora.tipo.nome
                    valor = lcnMora.valor
                    lancamento = lcnMora
                }
                fatRh.addToItens itemMora
                fatRh.save()
            }
        }
    }

    private Corte fechar(Corte corte, Date dataCorte) {
        //Define data de fechamento (corte) para a data de processamento

        corte.status = StatusCorte.FECHADO

        Fechamento fechAberto = corte.fechamento

        Rh rh = corte.fechamento.programa

        //Se próximo fechamento
        Fechamento fechProx
        def fechList = rh.fechamentos.sort { it.diaCorte }
        def fechIter = fechList.iterator()
        if (fechIter.hasNext())
            fechProx = fechIter.next()
        else
            fechProx = fechList[0]

        Calendar cal = dataCorte.toCalendar()

        if (fechProx.diaCorte > cal.get(Calendar.DAY_OF_MONTH)) {
            cal.set(Calendar.DAY_OF_MONTH, fechProx.diaCorte)
        } else {
            cal.set(Calendar.DAY_OF_MONTH, fechProx.diaCorte)
            cal.add(Calendar.MONTH, 1)
        }

        Corte corteProx = new Corte()
        corteProx.with {
            fechamento = fechProx
            dataPrevista = cal.time.clearTime()
            dataFechamento = cal.time.clearTime()
            dataCobranca = cal.time.clearTime() + rh.prazoPgtFatura
            dataInicioCiclo = corte.dataFechamento + 1
            status = StatusCorte.ABERTO
        }
        corteProx.save()
        corteProx

    }

    Corte getCorteAberto(Rh rh) {

        if (! rh.fechamentos)
            throw new RuntimeException("Nao ha dias de fechamento definidos para o programa $this")

        Corte corteAberto = Corte.withCriteria(uniqueResult: true) {
                                'in'("fechamento", rh.fechamentos)
                                eq("status", StatusCorte.ABERTO)
                            }

        //Senão houver corte aberto, cria primeiro Corte
        if (! corteAberto) {

            def dataProc = ReferenceDateProcessing.calcuteReferenceDate()
            def diaProc = dataProc[Calendar.DAY_OF_MONTH]
            def mesProc = dataProc[Calendar.MONTH]

            def diaAnt = 1
            Fechamento ciclo, cicloAnterior

            def ordFechList = rh.fechamentos.sort { it.diaCorte }

            ordFechList.find { f ->
                if (diaAnt <= diaProc && diaProc < f.diaCorte) {
                    ciclo = f
                    return true
                }
                diaAnt = f.diaCorte
                cicloAnterior = f
                return false
            }
            //Se ciclo não encontrado, volta para o primeiro

            //Calcula Data Prevista pra Corte
            def dataCorte = dataProc
            def datInicCiclo = dataProc
            def cal = dataCorte.toCalendar()
            def calInic = datInicCiclo.toCalendar()

            if (! ciclo) {
                ciclo = ordFechList[0]
                cal.set(Calendar.DAY_OF_MONTH, ciclo.diaCorte)
                cal.set(Calendar.MONTH, mesProc + 1)
            } else {
                cal.set(Calendar.DAY_OF_MONTH, ciclo.diaCorte)
                cal.set(Calendar.MONTH, mesProc)
            }
            dataCorte = cal.time

            //Calcula Data Inicio Ciclo
            if (! cicloAnterior)
                cicloAnterior = ordFechList[ordFechList.size() - 1]
            if (cicloAnterior.diaCorte > ciclo.diaCorte)
                calInic.set(Calendar.MONTH, mesProc - 1)
            else
                calInic.set(Calendar.MONTH, mesProc)

            calInic.set(Calendar.DAY_OF_MONTH, cicloAnterior.diaCorte + 1)
            datInicCiclo = calInic.time

            corteAberto = new Corte()
            corteAberto.with {
                dataPrevista = dataCorte
                dataCobranca = dataCorte + rh.prazoPgtFatura
                dataInicioCiclo = datInicCiclo
                status = StatusCorte.ABERTO
                fechamento = ciclo
            }
            corteAberto.save flush: true
            log.info "Corte $corteAberto criado"
        }
        return corteAberto
    }

}
