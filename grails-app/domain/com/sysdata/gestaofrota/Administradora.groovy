package com.sysdata.gestaofrota

class Administradora extends Participante {

	String bin
	
	static hasMany=[role:Role]
    static constraints = {
		bin(nullable:true)
    }
	
}
