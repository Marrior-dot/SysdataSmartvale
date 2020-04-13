package com.sysdata.gestaofrota

enum TipoCombustivel {
	GASOLINA("Gasolina"),
	ALCOOL("Álcool"),
	DIESEL("Diesel"),
	BIODIESEL("Biodiesel")
	
	String nome
	
	TipoCombustivel(nome){
		this.nome=nome
	}
	
	static asList(){
		[GASOLINA,ALCOOL,DIESEL,BIODIESEL]
	}
}