package com.sysdata.gestaofrota

enum StatusLancamento {

	A_FATURAR("A Faturar"),
	EFETIVADO("Efetivado"),
	ESTORNADO("Estornado"),
	FATURADO("Faturado"),
	LIQUIDADO("Liquidado")

	String nome
	
	StatusLancamento(nome){
		this.nome=nome
	}
	
}
