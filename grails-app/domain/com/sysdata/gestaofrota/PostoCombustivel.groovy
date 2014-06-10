package com.sysdata.gestaofrota

class PostoCombustivel extends Empresa {
	
	Double taxaReembolso=0.0
	
	TipoReembolso tipoReembolso

	static belongsTo=Rh
	
	static hasMany=[estabelecimentos:Estabelecimento,reembolsos:Reembolso,programas:Rh]
	
    static constraints = {
		tipoReembolso(nullable:true)
    }
}
