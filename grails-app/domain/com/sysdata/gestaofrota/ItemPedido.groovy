package com.sysdata.gestaofrota

class ItemPedido {

    static belongsTo = [pedido: PedidoCarga]
    static transients = ['funcionario']

    Participante participante
    Double valor
    Double sobra
    boolean ativo
    Lancamento lancamento


    static constraints = {
        lancamento nullable: true
    }

    Funcionario getFuncionario(){
        Funcionario.get(participante.id)
    }
}
