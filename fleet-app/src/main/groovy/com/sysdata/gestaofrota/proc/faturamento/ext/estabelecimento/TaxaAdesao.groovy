package com.sysdata.gestaofrota.proc.faturamento.ext.estabelecimento

import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento
import groovy.util.logging.Slf4j

@Slf4j
class TaxaAdesao implements ExtensaoFaturamento {

    @Override
    void gerarLancamento(Map ctx) {

        PagamentoEstabelecimento pagamentoEstab = ctx.pagamento
        PostoCombustivel estab = pagamentoEstab.estabelecimento

        LancamentoEstabelecimento lctoAdesao = new LancamentoEstabelecimento()
        lctoAdesao.with {
            dataEfetivacao = pagamentoEstab.dataProgramada
            dataPrevista = pagamentoEstab.dataProgramada
            valor = -estab.taxaAdesao
            tipo = TipoLancamento.TAXA_ADESAO_EC
            status = StatusLancamento.FATURADO
            conta = estab.conta
            pagamento = pagamentoEstab
        }
        lctoAdesao.save(flush: true)
        log.debug "\t\tADESAO #${lctoAdesao.id} val:${Util.formatCurrency(lctoAdesao.valor)}"
        pagamentoEstab.valor += lctoAdesao.valor.abs()

   }

    @Override
    void calcularValor(Map ctx) {

        PostoCombustivel estab = ctx.estabelecimento
        if (estab.taxaAdesao > 0.0) {
            Conta contaEstab = ctx.conta

            def jaCobrada = LancamentoEstabelecimento.createCriteria().count {
                                eq("tipo", TipoLancamento.TAXA_ADESAO_EC)
                                eq("status", StatusLancamento.FATURADO)
                                eq("conta", contaEstab)
                            } > 0

            if (!jaCobrada) {
                def aux = [handler: TaxaAdesao, valor: estab.taxaAdesao]
                ctx.extensoes = !ctx.extensoes ? [] << aux : ctx.extensoes << aux
            }
        }
    }
}