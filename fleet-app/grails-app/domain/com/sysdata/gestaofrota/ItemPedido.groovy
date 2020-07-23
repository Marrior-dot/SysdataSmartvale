package com.sysdata.gestaofrota

class ItemPedido {

    BigDecimal valor
    BigDecimal sobra = 0
    boolean ativo
    Lancamento lancamento
    TipoItemPedido tipo

    static belongsTo = [pedido: PedidoCarga]


    static constraints = {
        lancamento nullable: true
        tipo nullable: true
    }


    static mapping = {
        id generator: 'sequence', params: [sequence: "itempedido_seq"]
    }
}
