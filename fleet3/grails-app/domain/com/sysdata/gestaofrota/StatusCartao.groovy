package com.sysdata.gestaofrota

enum StatusCartao {
	CRIADO("Criado"),
	EMBOSSING("Embossing"),
	ATIVO("Ativo"),
	BLOQUEADO("Bloqueado"),
	CANCELADO("Cancelado")
	
	String nome
	
	StatusCartao(nome){
		this.nome=nome
	}
	
}
