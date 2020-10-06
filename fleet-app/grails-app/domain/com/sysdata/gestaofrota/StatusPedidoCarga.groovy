package com.sysdata.gestaofrota

enum StatusPedidoCarga {
	NOVO("Novo"),
	AGENDADO("Agendado"),
	COBRANCA("Cobrança"),
	PAGO("PAGO"),
	LIBERADO("Liberado"),
	CANCELADO("Cancelado"),
	FINALIZADO("Finalizado")
	
	String nome
	
	StatusPedidoCarga(nome){
		this.nome=nome
	}
	

}
