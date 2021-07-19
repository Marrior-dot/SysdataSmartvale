package com.sysdata.gestaofrota

class Endereco {

	String logradouro
	String numero
	String complemento
	String bairro
	Cidade cidade
	String cep
	
    static constraints = {
		complemento(nullable:true)
		cidade(nullable:true)
		cep(nullable:true)
    }
	
	String toString(){
		def flat=""
		this.properties.each{
			flat+="${it}\n"
		}
		flat
	}
}
