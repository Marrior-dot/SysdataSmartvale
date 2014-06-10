package com.sysdata.gestaofrota

class ItemPedido {

	static belongsTo=[pedido:PedidoCarga]
	
	Participante participante
	Double valor
	Double sobra
	boolean ativo
	
    static constraints = {
    }
}
