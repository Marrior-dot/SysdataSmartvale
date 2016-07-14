package com.sysdata.gestaofrota

class CategoriaFuncionario {

	String nome
	Double valorCarga
	Date dateCreated
	
	static belongsTo=[rh:Rh]
	static transients=['funcionarios']

	
    static constraints = {
    }
	
	static mapping={
		id generator:'sequence',params:[sequence:'categfuncionario_seq']
	}

	List<Funcionario> getFuncionarios(){
		Funcionario.findAllByCategoria(this)
	}
}
