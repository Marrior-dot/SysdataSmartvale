package com.sysdata.gestaofrota.proc.faturamento

import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.ReferenceDateProcessing
import com.sysdata.gestaofrota.proc.faturamento.boleto.GeradorBoleto
import com.sysdata.gestaofrota.proc.faturamento.boleto.GeradorBoletoFactory
import grails.boot.GrailsApp
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class CorteService {

    PortadorCorteService portadorCorteService
    GrailsApplication grailsApplication

    /**
     *  Recupera todas as contas de portadores que possuem lançamentos a faturar no ciclo atual
     *
     */

    def faturar(Corte cortePortador, Date dataCorte) {

        log.info "Faturando Corte $cortePortador ..."

        def contasId = LancamentoPortador.withCriteria {
                            projections {
                                groupProperty("conta.id")
                            }
                            eq("status", StatusLancamento.A_FATURAR)
                            conta {
                                portador {
                                    unidade {
                                        rh {
                                            fechamentos {
                                                eq("id", cortePortador.fechamento.id)
                                            }
                                        }
                                    }
                                }
                            }
                            order("conta.id")
                        }

        if (contasId.isEmpty())
            log.info "Não há contas a faturar para este corte"
        else {
            cortePortador.dataFechamento = dataCorte

            log.info "Total de Contas a Faturar: ${contasId.size()}"
            Conta contaRh = cortePortador.fechamento.programa.conta
            Fatura fatRh = new Fatura()
            fatRh.with {
                conta = contaRh
                data = dataCorte
                dataVencimento = cortePortador.dataCobranca
                status = StatusFatura.ABERTA
                tipo = TipoFatura.CONVENIO_POSPAGO
            }
            fatRh.corte = cortePortador

            tratarAtraso(fatRh, dataCorte)

            def itensFatRh = [:]

            // Fatura cada conta de portador individualmente
            contasId.each {
                Conta conta = Conta.get(it)
                Fatura fatPort = portadorCorteService.faturar(conta.portador, cortePortador, dataCorte)


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
                    status = StatusLancamento.FATURADO
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

            tratarTaxas(fatRh)



            if (grailsApplication.config.projeto.faturamento.portador.boleto.gerar) {

                fatRh.statusGeracaoBoleto = StatusGeracaoBoleto.GERAR
                fatRh.save()

                Boleto boleto = new Boleto()
                boleto.with {
                    fatura = fatRh
                    dataVencimento = fatRh.dataVencimento
                    valor = fatRh.valorTotal
                }
                boleto.save(flush: true)

                GeradorBoleto geradorBoleto = GeradorBoletoFactory.getGerador()
                geradorBoleto.gerarBoleto(boleto)
            }


            //Fecha última fatura e transfere saldo

            CortePortador proxCorte = fechar(cortePortador, dataCorte)

            //Gerar próximo corte
            def totalFatura = fatRh.valorTotal

            LancamentoConvenio fechamento = new LancamentoConvenio()
            fechamento.with {
                conta = fatRh.conta
                valor = -totalFatura
                tipo = TipoLancamento.FECHAMENTO_FATURA
                dataEfetivacao = dataCorte
                corte = fatRh.corte
                status = StatusLancamento.FATURADO
            }
            fechamento.save(flush: true)
            LancamentoConvenio saldoAnterior = new LancamentoConvenio()
            saldoAnterior.with {
                conta = fatRh.conta
                valor = totalFatura
                tipo = TipoLancamento.SALDO_ANTERIOR
                dataEfetivacao = dataCorte
                corte = proxCorte
                status = StatusLancamento.A_FATURAR
            }
            saldoAnterior.save(flush: true)
        }
        cortePortador.save(flush: true)
    }

    private void tratarTaxas(Fatura fatRh, Date dataProc) {
        CortePortador cortePortador = fatRh.corte as CortePortador
        Rh cliente = cortePortador.fechamento.programa
        if (cliente.taxaDesconto) {

            def valorDesconto = fatRh.valorTotal * cliente.taxaDesconto / 100


        }


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
                    status = StatusLancamento.FATURADO
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
                    status = StatusLancamento.FATURADO
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

    private CortePortador fechar(Corte corte, Date dataCorte) {
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

        CortePortador corteProx = new CortePortador()
        corteProx.with {
            fechamento = fechProx
            dataPrevista = cal.time.clearTime()
            dataFechamento = cal.time.clearTime()
            dataCobranca = cal.time.clearTime() + fechProx.diasAteVencimento
            dataInicioCiclo = corte.dataFechamento + 1
            status = StatusCorte.ABERTO
        }
        corteProx.save(flush: true)
        corteProx

    }

    CortePortador getCorteAberto(Rh rh) {

        if (! rh.fechamentos)
            throw new RuntimeException("Nao ha dias de fechamento definidos para o programa $this")

        CortePortador corteAberto = Corte.withCriteria(uniqueResult: true) {
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

            corteAberto = new CortePortador()
            corteAberto.with {
                dataPrevista = dataCorte
                dataCobranca = dataCorte + ciclo.diasAteVencimento
                dataInicioCiclo = datInicCiclo
                status = StatusCorte.ABERTO
                fechamento = ciclo
            }
            corteAberto.save flush: true
            log.info "Corte $corteAberto criado"
        }
        return corteAberto
    }

    def desfaturar(CortePortador corte) {
        log.info "Iniciando Desfaturamento Corte #${corte.id}..."
        Corte corteAberto = corte.fechamento.corteAberto
        def temLancamentos = LancamentoPortador.withCriteria {
                                projections {
                                    rowCount('id')
                                }
                                eq("corte", corteAberto)
                            }[0] > 0
        if (temLancamentos)
            throw new RuntimeException("Corte #${corte.id} não pode ser desfaturado! Corte aberto já possui lançamentos agendados")

        LancamentoConvenio saldoAnterior = LancamentoConvenio.withCriteria(uniqueResult: true) {
                                                eq("corte", corteAberto)
                                                eq("tipo", TipoLancamento.SALDO_ANTERIOR)
                                            }
        if (saldoAnterior) {
            log.info "(-) LC #${saldoAnterior.id} ${saldoAnterior.tipo}"
            saldoAnterior.delete()
        }

        def lancamentosConvenio = LancamentoConvenio.withCriteria() {
                                        eq("corte", corte)
                                        eq("status", StatusLancamento.FATURADO)
                                    }
        lancamentosConvenio.each {
            log.info "(-) LC #${it.id} ${it.tipo}"
            it.delete()
        }
        log.info "(-) COR AB #${corteAberto.id}"
        corteAberto.delete(flush: true)
        def faturasIds = Fatura.withCriteria {
                            projections {
                                property "id"
                            }
                            eq("corte", corte)
                        }
        faturasIds.eachWithIndex{ fatId, idx ->
            Fatura fatura = Fatura.get(fatId)
            log.info "Deletando boletos da fatura #${fatId}..."
            fatura.boletos.collect().each {
                log.info "(-) BOL #${it.id}"
                fatura.removeFromBoletos(it)
            }
            log.info "Deletando itens da fatura ${fatId}..."
            fatura.itens.collect().each {
                log.info "(-) IT FAT #${it.id}"
                fatura.removeFromItens(it)
            }
            log.info "(-) FAT #${fatId}"
            fatura.delete()
            if ((idx + 1) % 50 == 0)
                clearSession()
        }
        if (!corte.isAttached())
            corte.attach()
        log.info "Desfaturando lançamentos..."
        // Lançamentos Faturados
        def lancamentosIds = LancamentoPortador.withCriteria {
            projections {
                property "id"
            }
            eq("corte", corte)
            eq("status", StatusLancamento.FATURADO)
        }
        lancamentosIds.eachWithIndex{ lid, idx ->
            LancamentoPortador lancamentoPortador = LancamentoPortador.get(lid)
            lancamentoPortador.status = StatusLancamento.A_FATURAR
            lancamentoPortador.save()
            log.info "LC #${lid} dfat"
            if ((idx + 1) % 50 == 0)
                clearSession()
        }
        if (! corte.isAttached())
            corte.attach()

        corte.dataFechamento = null
        corte.status = StatusCorte.ABERTO
        corte.save(flush: true)
        log.info "Corte #${corte.id} desfaturado"
    }

}
