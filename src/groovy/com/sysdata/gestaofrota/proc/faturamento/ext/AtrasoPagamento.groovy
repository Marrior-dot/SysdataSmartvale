package com.sysdata.gestaofrota.proc.faturamento.ext

import com.sysdata.gestaofrota.Conta
import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.LancamentoPortador
import com.sysdata.gestaofrota.Portador
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

            }
        }

        return null
    }
}
