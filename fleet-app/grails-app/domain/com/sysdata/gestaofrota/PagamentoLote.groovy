package com.sysdata.gestaofrota

class PagamentoLote {

    Date dateCreated

    static belongsTo = [lotePagamento: LotePagamento]

    static constraints = {
    }
}
