package com.sysdata.gestaofrota

enum StatusArquivo {
	
	PROCESSANDO("Processando"),
	GERADO("Gerado"),
	PROCESSADO("Processado"),
	REGERADO("Regerado"),
	ENVIADO("Enviado")

	String name
	
	StatusArquivo(name){
		this.name=name
	}
	
	
}
