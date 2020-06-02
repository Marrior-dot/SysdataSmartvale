package com.sysdata.gestaofrota

enum StatusControleAutorizacao {
	PENDENTE("Pendente"),
	CONFIRMADA("Confirmada"),
	DESFEITA("Desfeita"),
	CANCELADA("Cancelada"),
	NEGADA("Negada"),
	NAO_AGENDAR("Não agendar")
	
	String nome
	
	StatusControleAutorizacao(nome){
		this.nome=nome
	}
	
	static asList(){
		[PENDENTE,CONFIRMADA,DESFEITA]
	}
	
}
