package com.sysdata.gestaofrota

enum StatusTransacao {
	AGENDAR("Não Processada"),
	AGENDADA("Processada"),
	CANCELADA("Cancelada"),
	ESTORNADA("Estornada"),
	NAO_AGENDAR("Não Agendar")
	
	String nome
	
	StatusTransacao(nome){
		this.nome=nome
	}
	
	static asList(){
		[AGENDAR,AGENDADA,CANCELADA,ESTORNADA]
	}
	
}
