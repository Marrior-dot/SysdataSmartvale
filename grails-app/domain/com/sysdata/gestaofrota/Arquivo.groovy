package com.sysdata.gestaofrota

class Arquivo {
	
	String nome
	Date dateCreated
	TipoArquivo tipo
	StatusArquivo status
	String conteudo
	Integer lote
	
    static constraints = {
		conteudo(nullable:true)
		lote nullable:true
    }
	
	static mapping={
		id generator:'sequence',params:[sequence:'arquivo_seq']
		conteudo type:'text' 
	}

	static Integer nextLote(TipoArquivo tipo){
		def lote=Arquivo.withCriteria(uniqueResult:true){
			projections{
				max('lote')
			}
			eq("tipo",tipo)
		}
		lote?lote+1:1
	}
}
