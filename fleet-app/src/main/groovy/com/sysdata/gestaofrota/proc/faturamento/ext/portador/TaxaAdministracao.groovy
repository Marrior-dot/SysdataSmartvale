package com.sysdata.gestaofrota.proc.faturamento.ext.portador

import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento

/**
 * Created by acception on 21/03/18.
 */
class TaxaAdministracao implements ExtensaoFaturamento {

    void tratar(ctx) {
        Conta cnt = ctx.fatura.conta
        Portador portador = ctx.portador
        def taxAdm = portador.unidade.rh.taxaAdministracao
        if (taxAdm > 0) {

            Corte corteAtual = ctx.fatura.corte
            def totalAprov = Transacao.withCriteria(uniqueResult: true) {
                projections {
                    sum("valor")
                }
                cartao {
                    eq("portador", portador)
                }
                'in'("statusControle", [StatusControleAutorizacao.CONFIRMADA,
                                        StatusControleAutorizacao.PENDENTE])
                ge("dataHora", corteAtual.dataInicioCiclo)
                le("dataHora", corteAtual.dataFechamento)
            }
            if (totalAprov > 0) {

                Fatura fatura = ctx.fatura
                def valTaxAdm = (totalAprov * taxAdm / 100.00).round(2)
                LancamentoPortador lcnTaxaAdm = new LancamentoPortador()
                lcnTaxaAdm.with {
                    corte = corteAtual
                    conta = cnt
                    tipo = TipoLancamento.TAXA_ADM
                    dataEfetivacao = ctx.dataProcessamento
                    valor = valTaxAdm
                    status = StatusLancamento.EFETIVADO
                    statusFaturamento = StatusFaturamento.FATURADO
                }
                lcnTaxaAdm.save()

                ItemFatura itTxAdm = new ItemFatura()
                itTxAdm.with {
                    data = lcnTaxaAdm.dataEfetivacao
                    descricao = lcnTaxaAdm.tipo.nome
                    valor = lcnTaxaAdm.valor
                    lancamento = lcnTaxaAdm
                }
                fatura.addToItens itTxAdm
                fatura.save()
            }
        }

    }

    @Override
    void gerarLancamento(Map ctx) {

    }

    @Override
    void calcularValor(Map ctx) {

    }
}
