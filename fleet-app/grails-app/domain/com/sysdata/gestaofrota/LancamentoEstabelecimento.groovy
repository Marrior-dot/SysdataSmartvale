package com.sysdata.gestaofrota

class LancamentoEstabelecimento extends Lancamento {

    Date dataPrevista
    BigDecimal valorTaxa
    PagamentoEstabelecimento pagamento


    static constraints = {
        dataPrevista nullable: true
        valorTaxa nullable: true, scale: 6
        pagamento nullable: true
    }

    static mapping = {
        dataPrevista type: 'date'
    }

    static namedQueries = {

        lancamentoPagamento { pg ->
            projections {
                property("id")
            }
            eq("pagamento", pg)
        }
    }
}
