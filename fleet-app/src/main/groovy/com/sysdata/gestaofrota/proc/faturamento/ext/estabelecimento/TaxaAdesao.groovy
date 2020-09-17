package com.sysdata.gestaofrota.proc.faturamento.ext.estabelecimento

import com.sysdata.gestaofrota.Conta
import com.sysdata.gestaofrota.LancamentoEstabelecimento
import com.sysdata.gestaofrota.PostoCombustivel
import com.sysdata.gestaofrota.StatusLancamento
import com.sysdata.gestaofrota.TipoLancamento
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento

class TaxaAdesao implements ExtensaoFaturamento {

    @Override
    void tratar(def ctx) {

        PostoCombustivel estab = ctx.estabelecimento

        if (estab.taxaAdesao > 0.0) {
            Conta contaEstab = ctx.conta

            def jaCobrada = LancamentoEstabelecimento.createCriteria().count {
                                eq("tipo", TipoLancamento.TAXA_ADESAO_EC)
                                eq("status", StatusLancamento.FATURADO)
                                eq("conta", contaEstab)
                            } > 0

            if (!jaCobrada) {
                LancamentoEstabelecimento lctoAdesao = new LancamentoEstabelecimento()
                lctoAdesao.with {
                    dataEfetivacao = ctx.dataCorte
                    dataPrevista = ctx.dataCorte
                    valor = -estab.taxaAdesao
                    tipo = TipoLancamento.TAXA_ADESAO_EC
                    status = StatusLancamento.FATURADO
                    conta = contaEstab
                    pagamento = ctx.pagamento
                }
                lctoAdesao.save(flush: true)
            }
        }
   }
}