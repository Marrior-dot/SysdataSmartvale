package com.sysdata.gestaofrota

enum TipoTransacao {
	CONFIGURACAO_PRECO("Configuração de Preço"),
	COMBUSTIVEL("Combustível"),
	CARGA_SALDO("Carga de Saldo"),
	CANCELAMENTO("Cancelamento"),
	CONSULTA_PRECOS("Consulta de Preços"),
	SERVICOS("Serviços"),
	CANCELAMENTO_COMBUSTIVEL("Cancelamento combustível"),
	TRANSFERENCIA_SALDO("Transferência de Saldo")
	
	String nome
	
	TipoTransacao(nome){
		this.nome=nome
	}
	
	static asList(){
		[CONFIGURACAO_PRECO,COMBUSTIVEL,CARGA_SALDO,TRANSFERENCIA_SALDO]
	}
}
