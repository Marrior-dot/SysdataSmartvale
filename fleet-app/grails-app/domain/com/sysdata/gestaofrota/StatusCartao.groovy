package com.sysdata.gestaofrota

enum StatusCartao {
	CRIADO("Criado"),
	LIBERADO_EMBOSSING("Liberado p/ Embossing"),
	EMBOSSING("Embossing"),
	ATIVO("Ativo"),
	BLOQUEADO("Bloqueado"),
	CANCELADO("Cancelado")
	
	String nome
	
	StatusCartao(nome){
		this.nome=nome
	}
	
}
