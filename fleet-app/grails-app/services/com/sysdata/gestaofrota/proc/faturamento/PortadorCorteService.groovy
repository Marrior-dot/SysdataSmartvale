package com.sysdata.gestaofrota.proc.faturamento

import com.sysdata.gestaofrota.Corte
import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.ItemFatura
import com.sysdata.gestaofrota.LancamentoCartao
import com.sysdata.gestaofrota.LancamentoPortador
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.StatusFatura
import com.sysdata.gestaofrota.StatusFaturamento
import com.sysdata.gestaofrota.StatusLancamento
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFactory
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento
import grails.gorm.transactions.Transactional
import grails.util.Holders

@Transactional
class PortadorCorteService {

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
                                eq("statusFaturamento", StatusFaturamento.NAO_FATURADO)
                                eq("corte", corte)
                                order("dataEfetivacao")
                            }
        Fatura fatura = ctx.fatura
        lctosAFat.each { lcto ->
            ItemFatura item = faturarLancamento(lcto)
            fatura.addToItens(item)
            lcto.statusFaturamento = StatusFaturamento.FATURADO
            lcto.status = StatusLancamento.EFETIVADO
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

}
