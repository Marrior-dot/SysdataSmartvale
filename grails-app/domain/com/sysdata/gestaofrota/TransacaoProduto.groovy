package com.sysdata.gestaofrota

class TransacaoProduto {

	BigDecimal precoUnitario
	
	static belongsTo=[transacao:Transacao,produto:Produto]
	
    static constraints = {
    }
	
	static mapping={
		id generator:"sequence",params:[sequence:"transacaoprod_seq"]
		version false
	}
}
