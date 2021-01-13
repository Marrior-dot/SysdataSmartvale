package com.sysdata.gestaofrota

enum TipoReembolso {
	
	INTERVALOS_MULTIPLOS("Intervalo Múltiplos"),
	SEMANAL("Semanal"),
	DIAS_FIXOS("Dias Fixos")

	String nome
	
	public TipoReembolso(nome){
		this.nome=nome
	}
}
