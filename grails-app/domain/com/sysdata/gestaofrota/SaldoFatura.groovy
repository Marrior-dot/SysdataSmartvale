package com.sysdata.gestaofrota

class SaldoFatura {

    Date dateCreated
    BigDecimal valorOriginal
    BigDecimal valorResidual
    TipoLancamento tipo

    static belongsTo = [fatura:Fatura]

    static constraints = {
    }
}
