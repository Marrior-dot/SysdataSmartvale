package com.sysdata.gestaofrota

enum StatusArquivo {
	
	PROCESSANDO("Processando"),
	GERADO("Gerado"),
	PROCESSADO("Processado")
	
	String name
	
	StatusArquivo(name){
		this.name=name
	}
	
	
}