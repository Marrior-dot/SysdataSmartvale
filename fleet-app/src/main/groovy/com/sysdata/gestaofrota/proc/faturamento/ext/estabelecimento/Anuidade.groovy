package com.sysdata.gestaofrota.proc.faturamento.ext.estabelecimento

import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento
import groovy.util.logging.Slf4j

@Slf4j
class Anuidade implements ExtensaoFaturamento {

    @Override
    void gerarLancamento(Map ctx) {

        PagamentoEstabelecimento pagamento = ctx.pagamento
        PostoCombustivel estab = pagamento.estabelecimento

        LancamentoEstabelecimento lctoAnuidade = new LancamentoEstabelecimento()
        lctoAnuidade.with {
            dataEfetivacao = ctx.dataCorte
            dataPrevista = ctx.dataCorte
            valor = -estab.anuidade
            tipo = TipoLancamento.ANUIDADE_EC
            status = StatusLancamento.FATURADO
            conta = estab.conta
            pagamento = ctx.pagamento

        }
        lctoAnuidade.save(flush: true)
        log.debug "\t\tANUIDADE #${lctoAnuidade.id} val:${Util.formatCurrency(lctoAnuidade.valor)}"
        pagamento.valor += lctoAnuidade.valor.abs()

    }

    @Override
    void calcularValor(Map ctx) {
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
                def aux = [handler: Anuidade, valor: estab.anuidade]
                ctx.extensoes = !ctx.extensoes ? [] << aux : ctx.extensoes << aux

            }
        }

    }
}