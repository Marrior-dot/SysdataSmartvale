package com.sysdata.gestaofrota

class AgendaPedido {

    static constraints = {
    }

    static mapping = {
        id generator: "sequence", params: [sequence: "agendapedido_seq"]
    }
}
