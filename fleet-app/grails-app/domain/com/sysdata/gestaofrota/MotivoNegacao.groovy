package com.sysdata.gestaofrota

class MotivoNegacao {

	String codigo
	String descricao
	String ocorrencia
	
    static constraints = {
		ocorrencia(nullable: true)
    }
	
	static mapping={
		id generator:"sequence",params:[sequence:"motivonegacao_seq"]
	}

	String toString() {
		"$codigo - $descricao"
	}
}
