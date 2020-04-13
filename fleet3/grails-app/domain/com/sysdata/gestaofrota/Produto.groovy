package com.sysdata.gestaofrota

class Produto {

	String codigo
	String nome
	TipoProduto tipo
	
    static constraints = {
		codigo unique:true,blank:false
		nome blank:false
		tipo blank:false
    }
	
	static mapping={
		id generator:"sequence",params:[sequence:"produto_seq"]
	}
}
