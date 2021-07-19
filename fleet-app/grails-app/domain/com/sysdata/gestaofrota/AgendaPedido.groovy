package com.sysdata.gestaofrota

class AgendaPedido {

    static belongsTo = [pedidoProgramado: PedidoCargaProgramado]

    static constraints = {
    }

    static mapping = {
        id generator: "sequence", params: [sequence: "agendapedido_seq"]
    }
}
