package com.sysdata.gestaofrota

enum TipoArquivo {
	
	EMBOSSING("Embossing"),
	CARTA_SENHA("Carta Senha")
	
	String name
	
	TipoArquivo(name){
		this.name=name
	}
	
}
