package com.sysdata.gestaofrota.proc.faturamento.ext.estabelecimento

import com.sysdata.gestaofrota.Conta
import com.sysdata.gestaofrota.LancamentoEstabelecimento
import com.sysdata.gestaofrota.PostoCombustivel
import com.sysdata.gestaofrota.StatusLancamento
import com.sysdata.gestaofrota.TipoLancamento
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento


class Anuidade implements ExtensaoFaturamento {

    @Override
    void tratar(Object ctx) {

        PostoCombustivel estab = ctx.estabelecimento

        if (estab.anuidade > 0.0) {
            Conta contaEstab = ctx.conta

            def dataCadastro = estab.dateCreated
            def data = ctx.dataCorte

            def dataUltimaCobranca = LancamentoEstabelecimento.withCriteria(uniqueResult: true) {
                                        projections {
                                            max("dataEfetivacao")
                                        }
                                        eq("tipo", TipoLancamento.ANUIDADE_EC)
                                        eq("status", StatusLancamento.FATURADO)
                                        eq("conta", contaEstab)
                                    }
            if ((dataUltimaCobranca && data - dataUltimaCobranca >= 365) || (data - dataCadastro >= 365)) {

                LancamentoEstabelecimento lctoAnuidade = new LancamentoEstabelecimento()
                lctoAnuidade.with {
                    dataEfetivacao = ctx.dataCorte
                    dataPrevista = ctx.dataCorte
                    valor = -estab.anuidade
                    tipo = TipoLancamento.ANUIDADE_EC
                    status = StatusLancamento.FATURADO
                    conta = contaEstab
                    pagamento = ctx.pagamento

                }
                lctoAnuidade.save(flush: true)
            }

        }
    }
}