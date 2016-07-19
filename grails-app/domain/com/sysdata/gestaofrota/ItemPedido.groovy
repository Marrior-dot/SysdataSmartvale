package com.sysdata.gestaofrota

class ItemPedido {

    static belongsTo = [pedido: PedidoCarga]
    static transients = ['funcionario']

    Participante participante
    Double valor
    Double sobra
    boolean ativo

    static constraints = {
    }

    Funcionario getFuncionario(){
        Funcionario.get(participante.id)
    }
}
