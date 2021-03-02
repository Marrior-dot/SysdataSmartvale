package com.sysdata.gestaofrota

class DadoBancario {
	
	Banco banco
	String agencia
	String conta
	String nomeTitular
	String documentoTitular
	TipoTitular tipoTitular
	
    static constraints = {
		nomeTitular nullable: true
		documentoTitular nullable: true
		tipoTitular nullable: true

    }
}
