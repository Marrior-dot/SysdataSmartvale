package com.sysdata.gestaofrota

enum TipoLancamento {
	CARGA("Carga"),
	COMPRA("Compra"),
	REEMBOLSO("Reembolso"),
	TAXA_UTILIZACAO("Taxa de Utilização"),
	TAXA_MANUTENCAO("Taxa de Manutenção"),
	MENSALIDADE("Mensalidade de Cartão"),
	EMISSAO_CARTAO("Emissão de Cartão"),
	REEMISSAO_CARTAO("Reemissão de Cartão"),
	TRANSFERENCIA_SALDO("Transferência de Saldo")

	String nome
	
	TipoLancamento(nome){
		this.nome=nome
	}
	
	static asList(){
		[CARGA,COMPRA,REEMBOLSO,TRANSFERENCIA_SALDO]
	}
}
