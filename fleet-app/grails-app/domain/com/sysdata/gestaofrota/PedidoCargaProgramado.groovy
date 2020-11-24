package com.sysdata.gestaofrota

class PedidoCargaProgramado extends PedidoCarga {

    StatusProgramacao statusProgramacao = StatusProgramacao.AGENDADO

    static hasMany = [agendas: AgendaPedido]

    static constraints = {
    }
}
