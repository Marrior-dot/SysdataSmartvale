package com.sysdata.gestaofrota

enum Status {
	ATIVO("Ativo"),
	INATIVO("Inativo"),
	BLOQUEADO("Bloqueado"),
	CANCELADO("Cancelado")
	
	String nome
	
	Status(nome){
		this.nome=nome
	}
	
	static asList(){
		[ATIVO,INATIVO,BLOQUEADO,CANCELADO]
	}
	
	static asBloqueado(){
		[ATIVO,BLOQUEADO]
	}

	static lista(){
		[ATIVO,BLOQUEADO,CANCELADO]
	}
}
