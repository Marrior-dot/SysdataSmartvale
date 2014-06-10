package com.sysdata.gestaofrota

enum TipoLancamento {
	CARGA("Carga"),
	COMPRA("Compra"),
	REEMBOLSO("Reembolso")
	
	String nome
	
	TipoLancamento(nome){
		this.nome=nome
	}
	
	static asList(){
		[CARGA,COMPRA,REEMBOLSO]
	}
}
