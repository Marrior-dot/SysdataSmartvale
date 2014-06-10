package com.sysdata.gestaofrota

enum Status {
	ATIVO("Ativo"),
	INATIVO("Inativo"),
	BLOQUEADO("Bloqueado")
	
	String nome
	
	Status(nome){
		this.nome=nome
	}
	
	static asList(){
		[ATIVO,INATIVO,BLOQUEADO]
	}
}
