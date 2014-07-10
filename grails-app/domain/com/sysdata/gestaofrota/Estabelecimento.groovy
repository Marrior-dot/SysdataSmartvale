package com.sysdata.gestaofrota

class Estabelecimento extends Empresa {

	String codigo
	
	static belongsTo=[empresa:PostoCombustivel]
	static hasMany=[precosCombustivel:PrecoCombustivel]
	
    static constraints = {
		codigo(unique:true)
    }
	
	static mapping={
		id generator:'sequence',params:[sequence:'estabelecimento_seq']
	}
}
