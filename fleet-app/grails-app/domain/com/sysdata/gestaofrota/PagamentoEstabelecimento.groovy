package com.sysdata.gestaofrota

/**
 * Representa cada pagamento de EC por Corte que agrupa todos os lançamentos para reembolsar em uma determinada data
 *
 */

class PagamentoEstabelecimento {

    Date dateCreated
    Date dataProgramada
    Date dataEfetivada
    PostoCombustivel estabelecimento
    CorteEstabelecimento corte
    BigDecimal valor

    static constraints = {
    }

    static mapping = {
        id generator: 'sequence', params: ['sequence': 'pagto_seq']
        dataProgramada type: 'date'
        dataEfetivada type: 'date'
    }
}
