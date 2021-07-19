package com.sysdata.gestaofrota

class ItemPedidoParticipante extends ItemPedido {

    Participante participante


    static transients = ['funcionario']

    static constraints = {
        participante nullable: true
    }

    Funcionario getFuncionario(){
        Funcionario.get(participante.id)
    }

}
