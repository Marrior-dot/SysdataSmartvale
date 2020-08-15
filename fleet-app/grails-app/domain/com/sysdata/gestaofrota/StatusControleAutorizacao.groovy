package com.sysdata.gestaofrota

enum StatusControleAutorizacao {
	PENDENTE("Pendente"),
	CONFIRMADA("Confirmada"),
	DESFEITA("Desfeita"),
	CANCELADA("Cancelada"),
	NEGADA("Negada")

	String nome
	
	StatusControleAutorizacao(nome){
		this.nome=nome
	}
	
	static asList(){
		[PENDENTE,CONFIRMADA,DESFEITA]
	}
	
}
