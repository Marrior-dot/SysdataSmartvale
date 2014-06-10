package com.sysdata.gestaofrota

enum StatusAutorizacao {
	AGENDAR("Agendar"),
	AGENDADA("Agendada"),
	CANCELADA("Cancelada"),
	ESTORNADA("Estornada")
	
	String nome
	
	StatusAutorizacao(nome){
		this.nome=nome
	}
	
	static asList(){
		[AGENDAR,AGENDADA,CANCELADA,ESTORNADA]
	}
	
}
