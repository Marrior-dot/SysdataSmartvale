package com.sysdata.gestaofrota.proc.faturamento.ext.portador

import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento

/**
 * Created by acception on 20/03/18.
 */

class TaxaManutencao implements ExtensaoFaturamento {

    /**
     * Cobra taxa:
     * 1. Se portador de cartão (funcionário/máquina) estiver ATIVO
     * ou
     * 2. Se taxa de utilização já tiver sido cobrada
     */

    void tratar(ctx) {

        Conta cnt = ctx.fatura.conta
        Portador portador = ctx.portador

        def taxManut = portador.unidade.rh.taxaManutencao

        if (taxManut > 0) {

            Fatura fatura = ctx.fatura
            if (portador.ativo || fatura.itens.find { it.lancamento.tipo == TipoLancamento.TAXA_UTILIZACAO }) {
                LancamentoPortador lcnTaxManut = new LancamentoPortador()
                lcnTaxManut.with {
                    corte = fatura.corte
                    conta = cnt
                    tipo = TipoLancamento.TAXA_MANUTENCAO
                    dataEfetivacao = ctx.dataProcessamento
                    valor = taxManut
                    status = StatusLancamento.EFETIVADO
                    statusFaturamento = StatusFaturamento.FATURADO
                }
                lcnTaxManut.save(failOnError: true)


                ItemFatura itTxMan = new ItemFatura()
                itTxMan.with {
                    data = lcnTaxManut.dataEfetivacao
                    descricao = lcnTaxManut.tipo.nome
                    valor = lcnTaxManut.valor
                    lancamento = lcnTaxManut
                }
                fatura.addToItens itTxMan
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
