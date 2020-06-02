package com.sysdata.gestaofrota

enum TipoReembolso {
	
	INTERVALOS_MULTIPLOS("Intervalo Múltiplos"),
	SEMANAL("Semanal")
	
	String nome
	
	public TipoReembolso(nome){
		this.nome=nome
	}
}
