package com.sysdata.gestaofrota

class MotivoNegacao {

	String codigo
	String descricao
	String ocorrencia
	
    static constraints = {
    }
	
	static mapping={
		id generator:"sequence",params:[sequence:"motivonegacao_seq"]
	}
}
