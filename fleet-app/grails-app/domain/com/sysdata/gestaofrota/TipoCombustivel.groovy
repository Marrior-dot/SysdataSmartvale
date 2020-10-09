package com.sysdata.gestaofrota

enum TipoCombustivel {
	GASOLINA("GASOLINA"),
	ALCOOL("ALCOOL"),
	DIESEL("DIESEL"),
	BIODIESEL("BIODIESEL")
	
	String nome
	
	TipoCombustivel(nome){
		this.nome=nome
	}
	
	static asList(){
		[GASOLINA,ALCOOL,DIESEL,BIODIESEL]
	}
}