package com.sysdata.gestaofrota

class Participante {

	Conta conta=new Conta()
	String nome
	Endereco endereco
	Telefone telefone
	Status status=Status.ATIVO
	Date dateCreated
	DadoBancario dadoBancario
	
	static embedded=['endereco','telefone','dadoBancario']
	
    static constraints = {
		endereco(nullable:true)
		telefone(nullable:true)
		dadoBancario(nullable:true)
    }
	
	static mapping={
		id generator:'sequence',params:[sequence:'participante_seq']
		conta lazy:false
	}
	
}
