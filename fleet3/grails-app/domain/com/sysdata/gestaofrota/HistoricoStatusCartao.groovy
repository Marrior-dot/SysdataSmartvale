package com.sysdata.gestaofrota

class HistoricoStatusCartao {
	
	Date dateCreated
	Cartao cartao
	StatusCartao novoStatus

    static constraints = {
    }
	
	static mapping={
		id generator:"sequence",params:[sequence:"histstscart_seq"]
		version false
	}
}
