package com.sysdata.gestaofrota

class ItemPedido {

    BigDecimal valor
    BigDecimal sobra = 0
    boolean ativo
    TipoItemPedido tipo
    Lancamento lancamento

    static belongsTo = [pedido: PedidoCarga]

    static transients = ['portador']

    static constraints = {
        tipo nullable: true
        lancamento nullable: true
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: "itempedido_seq"]
    }

    Portador getPortador() {
        if (this.instanceOf(ItemPedidoParticipante))
            return (this as ItemPedidoParticipante).funcionario.portador
        else if (this.instanceOf(ItemPedidoMaquina))
            return (this as ItemPedidoMaquina).maquina.portador
    }
}
