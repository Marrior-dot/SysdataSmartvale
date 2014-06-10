package com.sysdata.gestaofrota

class Arquivo {
	
	String nome
	Date dateCreated
	TipoArquivo tipo
	StatusArquivo status
	String conteudo
	
    static constraints = {
		conteudo(nullable:true)
    }
	
	static mapping={
		id generator:'sequence',params:[sequence:'arquivo_seq']
		conteudo type:'text' 
	}
}
