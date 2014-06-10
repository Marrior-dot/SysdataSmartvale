package com.sysdata.gestaofrota

class Cidade {

	String nome
	
	static belongsTo=[estado:Estado]
	
    static constraints = {
    }
}
