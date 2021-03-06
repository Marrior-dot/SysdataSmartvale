package com.sysdata.gestaofrota

class DadoBancario {
	
	Banco banco
	String agencia
	String conta
	String nomeTitular
	String documentoTitular
	TipoTitular tipoTitular

	static transients = ['contaSemMascara']

    static constraints = {
		nomeTitular nullable: true
		documentoTitular nullable: true
		tipoTitular nullable: true
    }

	String getContaSemMascara() {
		if (this.conta.contains('-')) {
			def aux = this.conta.replaceAll('-', '')
			return aux
		} else
			return this.conta

	}
}
