package com.sysdata.gestaofrota

class Fatura {

    Corte corte
    Date dataVencimento
    Lancamento lancamento


    static hasMany = [itens:ItemFatura]

    static constraints = {
    }
}
