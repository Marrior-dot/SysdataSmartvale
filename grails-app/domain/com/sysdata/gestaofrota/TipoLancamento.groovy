package com.sysdata.gestaofrota

enum TipoLancamento {
	CARGA("Carga"),
	COMPRA("Compra"),
	REEMBOLSO("Reembolso"),
	TRANSFERENCIA_SALDO("Transferência de Saldo")
	
	String nome
	
	TipoLancamento(nome){
		this.nome=nome
	}
	
	static asList(){
		[CARGA,COMPRA,REEMBOLSO,TRANSFERENCIA_SALDO]
	}
}
