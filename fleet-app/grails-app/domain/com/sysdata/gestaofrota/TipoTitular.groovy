package com.sysdata.gestaofrota

enum TipoTitular {
	PESSOA_FISICA("Pessoa Física"),
	PESSOA_JURIDICA("Pessoa Jurídica")
	
	String nome
	
	TipoTitular(nome){
		this.nome=nome
	}
	
	static asList(){
		[PESSOA_FISICA,PESSOA_JURIDICA]
	}
}
