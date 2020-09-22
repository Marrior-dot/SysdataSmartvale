package com.sysdata.gestaofrota.proc.faturamento.ext.estabelecimento

import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento
import groovy.util.logging.Slf4j

/**
 * Taxa de Visibilidade será cobrada uma única vez no mês em que houver transação no EC
 *
 */

@Slf4j
class TaxaVisibilidade implements ExtensaoFaturamento {

    @Override
    void gerarLancamento(Map ctx) {

        PagamentoEstabelecimento pagamentoEstab = ctx.pagamento
        PostoCombustivel estab = pagamentoEstab.estabelecimento

        LancamentoEstabelecimento lctoTaxaVisibilidade = new LancamentoEstabelecimento()
        lctoTaxaVisibilidade.with {
            dataEfetivacao = pagamentoEstab.dataProgramada
            dataPrevista = pagamentoEstab.dataProgramada
            valor = -estab.taxaAdesao
            tipo = TipoLancamento.TAXA_VISIBILIDADE_EC
            status = StatusLancamento.FATURADO
            conta = estab.conta
            pagamento = pagamentoEstab
        }
        lctoTaxaVisibilidade.save(flush: true)
        log.debug "\t\tVISIBILIDADE #${lctoTaxaVisibilidade.id} val:${Util.formatCurrency(lctoTaxaVisibilidade.valor)}"
        pagamentoEstab.valor += lctoTaxaVisibilidade.valor.abs()

    }

    @Override
    void calcularValor(Map ctx) {
        PagamentoEstabelecimento pagamentoEstab = ctx.pagamento
        PostoCombustivel estab = pagamentoEstab.estabelecimento

        if (estab.taxaVisibilidade > 0.0) {
            Conta contaEstab = ctx.conta

            def dataFim = pagamentoEstab.dataProgramada
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
                def aux = [handler: TaxaVisibilidade, valor: estab.taxaVisibilidade]
                ctx.extensoes = !ctx.extensoes ? [] << aux : ctx.extensoes << aux
            }

        }

    }
}