package com.sysdata.gestaofrota

enum TipoAbastecimento {
	GASOLINA("Gasolina"),
	ALCOOL("Álcool"),
	DIESEL("Diesel"),
	BICOMBUSTIVEL("Flex")
	
	String nome
	
	TipoAbastecimento(nome){
		this.nome=nome
	}
	
	static asList(){
		[GASOLINA,ALCOOL,DIESEL,BICOMBUSTIVEL]
	}
}
