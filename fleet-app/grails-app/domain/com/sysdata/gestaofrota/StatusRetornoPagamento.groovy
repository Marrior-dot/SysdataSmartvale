package com.sysdata.gestaofrota

class StatusRetornoPagamento {

    String codigo
    String descricao


    static constraints = {
    }

    static mapping = {
        id generator: "sequence", params: [sequence: "stsretpgto_seq"]
    }
}
