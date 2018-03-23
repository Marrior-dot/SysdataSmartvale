package com.sysdata.gestaofrota.proc.faturamento.ext

import com.sysdata.gestaofrota.Conta
import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.ItemFatura
import com.sysdata.gestaofrota.LancamentoConvenio
import com.sysdata.gestaofrota.LancamentoPortador
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.StatusFaturamento
import com.sysdata.gestaofrota.StatusLancamento
import com.sysdata.gestaofrota.TipoLancamento

/**
 * Created by acception on 22/03/18.
 */
class AtrasoPagamento implements ExtensaoFaturamento {

    @Override
    def tratar(ctx) {

        Fatura fatura=ctx.fatura

        Conta cnt=ctx.conta
        Portador portador=cnt.participante as Portador
        def taxMulta=portador.unidade.rh.multaAtraso
        def taxMora=portador.unidade.rh.jurosProRata


        def pgtos=fatura.itens.findAll{it.lancamento.tipo==TipoLancamento.PAGAMENTO}.sort{it.data}
        if(pgtos){

            def saldoDevedor=ctx.ultimaFatura.valorTotal
            def dataCtrl=ctx.dataRefCob
            def multa=0.0
            def mora=0.0
            pgtos.each{pg->

                def delta=pg.data-dataCtrl

                if(delta>0 && saldoDevedor>0){
                    if(!multa) multa=(saldoDevedor*taxMulta/100).round(2)
                    mora+=(saldoDevedor*(taxMora/delta)/100).round(2)
                }

                saldoDevedor+=pg.valor
                if(pg.data>ctx.dataRefCob)
                    dataCtrl=pg.data

            }

            if(multa>0){
                LancamentoConvenio lcnMulta=new LancamentoConvenio()
                lcnMulta.with {
                    conta=cnt
                    tipo=TipoLancamento.MULTA
                    dataEfetivacao=new Date().clearTime()
                    valor=multa
                    status=StatusLancamento.EFETIVADO
                    statusFaturamento=StatusFaturamento.FATURADO
                }
                lcnMulta.save()
                ItemFatura itemMulta=new ItemFatura()
                itemMulta.with{
                    data=lcnMulta.dataEfetivacao
                    descricao=lcnMulta.tipo.nome
                    valor=lcnMulta.valor
                    lancamento=lcnMulta
                }
                fatura.addToItens itemMulta
                fatura.save()
            }
            if(mora>0){
                LancamentoConvenio lcnMora=new LancamentoConvenio()
                lcnMora.with {
                    conta=cnt
                    tipo=TipoLancamento.MORA
                    dataEfetivacao=new Date().clearTime()
                    valor=multa
                    status=StatusLancamento.EFETIVADO
                    statusFaturamento=StatusFaturamento.FATURADO
                }
                lcnMora.save()
                ItemFatura itemMora=new ItemFatura()
                itemMora.with{
                    data=lcnMora.dataEfetivacao
                    descricao=lcnMora.tipo.nome
                    valor=lcnMora.valor
                    lancamento=lcnMora
                }
                fatura.addToItens itemMora
                fatura.save()
            }
        }
        return null
    }
}
