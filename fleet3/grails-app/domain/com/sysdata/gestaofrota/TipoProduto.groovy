package com.sysdata.gestaofrota

enum TipoProduto {
	COMBUSTIVEL("Combustível"),
	SERVICOS("Serviços Gerais")
	
	String nome
	
	TipoProduto(nome){
		this.nome=nome
	}
}
