package com.sysdata.gestaofrota

class Estado {

	String nome
	String uf
	
	static hasMany=[cidades:Cidade]
	
    static constraints = {
    }
}
