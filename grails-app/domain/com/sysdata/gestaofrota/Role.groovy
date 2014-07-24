package com.sysdata.gestaofrota

class Role {

	String authority
	
	static belongsTo=[owner:Participante]
	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
