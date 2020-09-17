package com.sysdata.gestaofrota

class LotePagamento {

    Date dateCreated

    static hasMany = [pagamentos: PagamentoLote, cortes: CorteEstabelecimento]
    
    static constraints = {
    }

    static mapping = {
        id generator: "sequence", params: ["sequence": "lotepag_seq"]
    }
}
