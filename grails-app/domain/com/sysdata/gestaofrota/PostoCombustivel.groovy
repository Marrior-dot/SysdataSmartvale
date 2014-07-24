package com.sysdata.gestaofrota

class PostoCombustivel extends Empresa {
	
	Double taxaReembolso=0.0
	
	TipoReembolso tipoReembolso

	static belongsTo=Rh
	
	static hasMany=[estabelecimentos:Estabelecimento,reembolsos:Reembolso,programas:Rh,role:Role]
	
    static constraints = {
		tipoReembolso(nullable:true)
    }
}
