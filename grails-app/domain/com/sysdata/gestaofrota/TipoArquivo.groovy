package com.sysdata.gestaofrota

enum TipoArquivo {
	
	EMBOSSING("Embossing"),
	CARTA_SENHA("Carta Senha"),
	REMESSA_COBRANCA("Arquivo Remessa")
	
	String name
	
	TipoArquivo(name){
		this.name=name
	}
	
}
