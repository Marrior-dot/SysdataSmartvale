package com.sysdata.gestaofrota

enum StatusLancamento {

	A_FATURAR("A Faturar"),
	EFETIVADO("Efetivado"),
	ESTORNADO("Estornado"),
	FATURADO("Faturado")

	String nome
	
	StatusLancamento(nome){
		this.nome=nome
	}
	
	static asList(){
		[A_FATURAR,EFETIVADO,ESTORNADO]
	}
	
}
