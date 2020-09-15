package com.sysdata.gestaofrota.proc.faturamento.ext.estabelecimento

import com.sysdata.gestaofrota.Conta
import com.sysdata.gestaofrota.LancamentoEstabelecimento
import com.sysdata.gestaofrota.PostoCombustivel
import com.sysdata.gestaofrota.StatusLancamento
import com.sysdata.gestaofrota.TipoLancamento
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento

/**
 * Taxa de Visibilidade será cobrada uma única vez no mês em que houver transação no EC
 *
 */

class TaxaVisibilidade implements ExtensaoFaturamento {

    @Override
    void tratar(Object ctx) {

        PostoCombustivel estab = ctx.estabelecimento

        if (estab.taxaVisibilidade > 0.0) {
            Conta contaEstab = ctx.conta

            def dataFim = ctx.dataCorte
            def mes = dataFim[Calendar.MONTH]
            def ano = dataFim[Calendar.YEAR]
            def dataInicio = new Date([dayOfMonth: 1, month: mes + 1, year: ano])

            def jaCobrada = LancamentoEstabelecimento.createCriteria().count {
                                eq("tipo", TipoLancamento.TAXA_VISIBILIDADE_EC)
                                eq("status", StatusLancamento.FATURADO)
                                eq("conta", contaEstab)
                                ge("dataEfetivacao", dataInicio)
                                le("dataEfetivacao", dataFim)
                            } > 0

            if (!jaCobrada) {
                LancamentoEstabelecimento lctoTaxaVisibilidade = new LancamentoEstabelecimento()
                lctoTaxaVisibilidade.with {
                    dataEfetivacao = ctx.dataCorte
                    dataPrevista = ctx.dataCorte
                    valor = -estab.taxaAdesao
                    tipo = TipoLancamento.TAXA_VISIBILIDADE_EC
                    status = StatusLancamento.FATURADO
                    conta = contaEstab
                    pagamento = ctx.pagamento
                }
                lctoTaxaVisibilidade.save(flush: true)
            }
        }
    }
}