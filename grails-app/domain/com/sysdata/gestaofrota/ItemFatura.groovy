package com.sysdata.gestaofrota

class ItemFatura {

    Date data
    String descricao
    BigDecimal valor
    Lancamento lancamento

    static belongsTo = [fatura:Fatura]


    static constraints = {
    }
}
