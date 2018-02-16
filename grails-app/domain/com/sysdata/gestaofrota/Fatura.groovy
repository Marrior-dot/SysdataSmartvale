package com.sysdata.gestaofrota

class Fatura {

    Corte corte
    Date data
    Date dataVencimento
    StatusFatura status

    static hasMany = [itens:ItemFatura]

    static transients = ['valorTotal']

    static constraints = {
    }

    BigDecimal getValorTotal() {
        this.itens.sum { it.valor }
    }

}
