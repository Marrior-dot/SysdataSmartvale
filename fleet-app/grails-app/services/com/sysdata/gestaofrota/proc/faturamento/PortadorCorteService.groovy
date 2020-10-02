package com.sysdata.gestaofrota.proc.faturamento

import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFactory
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento
import grails.gorm.transactions.Transactional
import grails.util.Holders

@Transactional
class PortadorCorteService {

    CorteService corteService

    private ItemFatura faturarLancamento(LancamentoCartao lcto) {
        ItemFatura item = new ItemFatura()
        item.with {
            descricao = lcto.extrato
            data = lcto.dataEfetivacao
            valor = lcto.valor
            lancamento = lcto
        }
        return item
    }

    Fatura faturar(Portador portador, Corte corte, Date dataCorte) {
        def fatConfig = Holders.grailsApplication.config.project.faturamento

        def ctx = initContext(corte, dataCorte)

        //Lançamentos a FATURAR
        def lctosAFat = LancamentoPortador.withCriteria {
                                eq("conta", portador.conta)
                                eq("status", StatusLancamento.A_FATURAR)
                                eq("corte", corte)
                                order("dataEfetivacao")
                            }
        Fatura fatura = ctx.fatura
        lctosAFat.each { lcto ->
            ItemFatura item = faturarLancamento(lcto)
            fatura.addToItens(item)
            lcto.status = StatusLancamento.FATURADO
        }
        //Roda extensoes
        fatConfig.extensoes.each { e ->
            ExtensaoFaturamento ext = ExtensaoFactory.getInstance(e)
            ext.tratar(ctx)
        }

        if (fatura.itens) {
            fatura.save(flush: true)

            Fatura ultFat = ctx.ultimaFatura
            if (ultFat) {
                ultFat.status = StatusFatura.FECHADA
                ultFat.save()
            }
            //Log fatura
            log.debug fatura
            fatura.itens.sort { it.data }.each { log.debug it }

        } else {
            fatura.discard()
            log.debug "CNT => #${fatura.conta.id} sem faturamento"
        }
        fatura
    }


    private def initContext(Portador portador, Corte corteAberto, Date dataProc) {
        def ctx = new Expando()

        ctx.dataProcessamento = dataProc

        //Fecha última fatura
        ctx.ultimaFatura = portador.conta.ultimaFatura
        ctx.atrasado = false

        //Data de referência para atraso pagamento
        ctx.dataRefCob = ctx.ultimaFatura ? ctx.ultimaFatura.dataVencimento : dataProc

        //Inicializa (cria) objeto Fatura
        Fatura fatura = new Fatura()
        fatura.with {
            conta = portador.conta
            dataVencimento = corteAberto.dataCobranca
            data = dataProc
            corte = corteAberto
            status = StatusFatura.ABERTA
        }
        ctx.fatura = fatura
        ctx.portador = portador

        /*
        ctx.addSaldo={tpSld,val->
            if(!ctx.novosSaldos.containsKey(tpSld)) ctx.novosSaldos[tpSld]=0.0
            ctx.novosSaldos[tpSld]+=val
        }*/
        ctx
    }

    Map findFaturaAberta(Portador portador) {

        def fatura = [:]

        Corte corteAberto = corteService.getCorteAberto(portador.unidade.rh)

        def aFaturarList = LancamentoPortador.withCriteria {
                                eq("conta", portador.conta)
                                eq("status", StatusLancamento.A_FATURAR)
                                eq("corte", corteAberto)
                                order("dataEfetivacao")
                            }

        def totalFatura = 0.0
        fatura.itens = []
        aFaturarList.each { lcto ->
            totalFatura += lcto.valor
            fatura.itens << [
                                "data": lcto.dataEfetivacao,
                                "descricao": lcto.extrato,
                                "valor": lcto.valor
                            ]
        }

        fatura.data = corteAberto.dataPrevista
        fatura.dataVencimento = corteAberto.dataPrevista + corteAberto.fechamento.diasAteVencimento
        fatura.valorTotal = totalFatura

        return fatura

    }

    Fatura findUltimaFatura(Portador portador) {
        return Fatura.findByContaAndStatus(portador.conta, StatusFatura.ABERTA)
    }

    List<Fatura> listarFaturasAnteriores(Portador portador) {
        return Fatura.where {
                        conta == portador.conta &&
                        status == StatusFatura.FECHADA
                }.list([sort: 'dataVencimento'])

    }

}
