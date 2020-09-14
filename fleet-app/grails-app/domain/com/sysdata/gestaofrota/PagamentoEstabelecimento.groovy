package com.sysdata.gestaofrota

class PagamentoEstabelecimento {

    Date dateCreated
    Date dataProgramada
    Date dataEfetivada
    Estabelecimento estabelecimento
    CorteEstabelecimento corte
    BigDecimal valor

    static constraints = {
    }

    static mapping = {
        id generator: 'sequence', params: ['sequence': 'pagto_seq']
    }
}
