package com.sysdata.gestaofrota

enum StatusLancamento {
	A_EFETIVAR("A Efetivar"),
	EFETIVADO("Efetivado"),
	ESTORNADO("Estornado"),
	A_PAGAR("A Pagar"),
	A_RECEBER("A Receber"),
	FATURADO("Faturado")



	String nome
	
	StatusLancamento(nome){
		this.nome=nome
	}
	
	static asList(){
		[A_EFETIVAR,EFETIVADO,ESTORNADO]
	}
	
}
