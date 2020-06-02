package com.sysdata.gestaofrota

enum StatusLancamento {
	A_EFETIVAR("A Efetivar"),
	EFETIVADO("Efetivado"),
	ESTORNADO("Estornado")
	
	String nome
	
	StatusLancamento(nome){
		this.nome=nome
	}
	
	static asList(){
		[A_EFETIVAR,EFETIVADO,ESTORNADO]
	}
	
}
