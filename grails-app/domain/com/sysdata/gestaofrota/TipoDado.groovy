package com.sysdata.gestaofrota

enum TipoDado {
	
	INTEGER("Integer"),
	LONG("Long"),
	FLOAT("Float"),
	DOUBLE("Double"),
	STRING("String")
	
	String nome
	
	TipoDado(nome){
		this.nome=nome
	}

}
