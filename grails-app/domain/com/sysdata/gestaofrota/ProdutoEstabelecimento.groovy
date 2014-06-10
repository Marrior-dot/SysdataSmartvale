package com.sysdata.gestaofrota

class ProdutoEstabelecimento {

	Double valor
	Double valorAnterior
	
	static belongsTo=[estabelecimento:Estabelecimento,produto:Produto]
	
	static constraints = {
	}
	
	static mapping={
		id generator:"sequence",params:[sequence:"produtoestab_seq"]
		version false
	}
}
