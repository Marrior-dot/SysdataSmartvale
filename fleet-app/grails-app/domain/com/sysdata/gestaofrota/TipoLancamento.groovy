package com.sysdata.gestaofrota

enum TipoLancamento {
	CARGA("Carga"),
	COMPRA("Compra"),
	REEMBOLSO("Reembolso"),
	TAXA_UTILIZACAO("Taxa de Utilização"),
	TAXA_MANUTENCAO("Taxa de Manutenção"),
	TAXA_ADM("Taxa Administração"),
	TAXA_DESCONTO("Taxa Desconto"),
	MENSALIDADE("Mensalidade de Cartão"),
	EMISSAO_CARTAO("Emissão de Cartão"),
	REEMISSAO_CARTAO("Reemissão de Cartão"),
	TRANSFERENCIA_SALDO("Transferência de Saldo"),
	PAGAMENTO("Pagamento"),
    MULTA("Multa"),
    MORA("Juros Moratórios"),
	FECHAMENTO_FATURA("Fechamento"),
	SALDO_ANTERIOR("Saldo Anterior"),

	//Taxas/Tarifas para EC
	TAXA_ADESAO_EC("Taxa de Adesão"),
	TAXA_VISIBILIDADE_EC("Taxa de Visibilidade"),
	ANUIDADE_EC("Anuidade"),
	TARIFA_BANCARIA_EC("Anuidade")

	String nome
	
	TipoLancamento(nome){
		this.nome=nome
	}
	
	static asList(){
		[CARGA,COMPRA,REEMBOLSO,TRANSFERENCIA_SALDO]
	}
}
