package com.sysdata.gestaofrota.proc.faturamento.ext.estabelecimento

import com.sysdata.gestaofrota.Conta
import com.sysdata.gestaofrota.LancamentoEstabelecimento
import com.sysdata.gestaofrota.PostoCombustivel
import com.sysdata.gestaofrota.StatusLancamento
import com.sysdata.gestaofrota.TipoLancamento
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento


class TarifaBancaria implements ExtensaoFaturamento {

    @Override
    void tratar(Object ctx) {

        PostoCombustivel estab = ctx.estabelecimento

        if (estab.tarifaBancaria > 0.0) {
            Conta contaEstab = ctx.conta

            LancamentoEstabelecimento lctoTarifaBancaria = new LancamentoEstabelecimento()
            lctoTarifaBancaria.with {
                dataEfetivacao = ctx.dataCorte
                dataPrevista = ctx.dataCorte
                valor = -estab.tarifaBancaria
                tipo = TipoLancamento.TARIFA_BANCARIA_EC
                status = StatusLancamento.FATURADO
                conta = contaEstab
                pagamento = ctx.pagamento
            }
            lctoTarifaBancaria.save(flush: true)
        }
    }
}