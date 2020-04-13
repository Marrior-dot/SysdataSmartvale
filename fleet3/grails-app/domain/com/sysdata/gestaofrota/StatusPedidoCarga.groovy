package com.sysdata.gestaofrota

enum StatusPedidoCarga {
	NOVO("Novo"),
	BOLETO_EMITIDO("Boleto Emitido"),
	PAGO("PAGO"),
	LIBERADO("Liberado"),
	CANCELADO("Cancelado"),
	FINALIZADO("Finalizado")
	
	String nome
	
	StatusPedidoCarga(nome){
		this.nome=nome
	}
	
	static asList(){
		[NOVO,BOLETO_EMITIDO,PAGO,LIBERADO,CANCELADO,FINALIZADO]
	}
	
}
