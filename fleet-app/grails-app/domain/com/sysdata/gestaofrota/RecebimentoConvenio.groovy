package com.sysdata.gestaofrota

class RecebimentoConvenio {

    Date dateCreated
    Date dataProgramada
    Date dataEfetivada
    Rh rh
    CorteConvenio corte
    BigDecimal valor
    BigDecimal valorBruto
    BigDecimal valorTaxaAdm
    StatusReenvioPagamento statusReenvio
    StatusPagamento status = StatusPagamento.AGENDADO

    static constraints = {
        dataEfetivada nullable: true
        statusReenvio nullable: true
        valor nullable: true
        valorBruto nullable: true
        valorTaxaAdm nullable: true
    }

    static mapping = {
        id generator: 'sequence', params: ['sequence': 'receb_seq']
        dataProgramada type: 'date'
        dataEfetivada type: 'date'
    }

}
