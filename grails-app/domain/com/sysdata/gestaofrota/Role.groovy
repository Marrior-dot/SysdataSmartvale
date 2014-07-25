package com.sysdata.gestaofrota

class Role {

	String authority
	String owner
	
	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
		owner nullable: true
	}
	
	static def listOwners(){
		["Processadora", "Administradora", "RH", "Estabelecimento"]
	}
}
