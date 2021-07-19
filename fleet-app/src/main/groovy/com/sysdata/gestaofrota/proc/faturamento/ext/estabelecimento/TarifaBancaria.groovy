package com.sysdata.gestaofrota.proc.faturamento.ext.estabelecimento

import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento
import groovy.util.logging.Slf4j

@Slf4j
class TarifaBancaria implements ExtensaoFaturamento {


    @Override
    void gerarLancamento(Map ctx) {
        PagamentoEstabelecimento pagamentoEstab = ctx.pagamento
        PostoCombustivel estab = pagamentoEstab.estabelecimento

        LancamentoEstabelecimento lctoTarifaBancaria = new LancamentoEstabelecimento()
        lctoTarifaBancaria.with {
            dataEfetivacao = pagamentoEstab.dataProgramada
            dataPrevista = pagamentoEstab.dataProgramada
            valor = -estab.tarifaBancaria
            tipo = TipoLancamento.TARIFA_BANCARIA_EC
            status = StatusLancamento.FATURADO
            conta = estab.conta
            pagamento = pagamentoEstab
        }
        lctoTarifaBancaria.save(flush: true)
        log.debug "\t\tTAR.BANCARIA #${lctoTarifaBancaria.id} val:${Util.formatCurrency(lctoTarifaBancaria.valor)}"
        pagamentoEstab.valor += lctoTarifaBancaria.valor.abs()

    }

    @Override
    void calcularValor(Map ctx) {
        PostoCombustivel estab = ctx.estabelecimento

        if (estab.tarifaBancaria > 0.0) {
            def aux = [handler: TarifaBancaria, valor: estab.tarifaBancaria]
            ctx.extensoes = !ctx.extensoes ? [] << aux : ctx.extensoes << aux
        }
    }
}