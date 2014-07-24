package com.sysdata.gestaofrota

class Rh extends Empresa {
	
	String codigo
	
	static hasMany=[unidades:Unidade,categoriasFuncionario:CategoriaFuncionario,empresas:PostoCombustivel,role:Role]
	
    static constraints = {
    }
	
	String toString(){
		def flat=""
		this.properties.each{
			flat+="${it}\n"
		}
		flat
	}
	
}
