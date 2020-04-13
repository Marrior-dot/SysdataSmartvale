package com.sysdata.gestaofrota

class PrecoCombustivel {

	TipoCombustivel tipoCombustivel
	Double valor
	Double valorAnterior
	
	static belongsTo=[estabelecimento:Estabelecimento]
	
    static constraints = {
    }
	
	static mapping={
		id generator:'sequence',params:[sequence:'precocombustivel_seq']
		version false
	}
}
