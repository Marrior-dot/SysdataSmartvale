package com.sysdata.gestaofrota

class ItemPedido {

    Participante participante
    BigDecimal valor
    BigDecimal sobra = 0
    boolean ativo
    Lancamento lancamento
    TipoItemPedido tipo

    static belongsTo = [pedido: PedidoCarga]
    static transients = ['funcionario']


    static constraints = {
        lancamento nullable: true
        tipo nullable: true
    }

    Funcionario getFuncionario(){
        Funcionario.get(participante.id)
    }
}
