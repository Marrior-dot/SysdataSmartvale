package com.sysdata.gestaofrota

class Funcionario extends Participante {

	String cpf
	String matricula
	String rg
	Date dataNascimento
	String cnh
	Date validadeCnh
	Telefone telefoneComercial
	CategoriaFuncionario categoria
	CategoriaCnh categoriaCnh
	boolean gestor

	static belongsTo=[unidade:Unidade]	
	static embedded=['telefoneComercial']
	static hasMany=[cartoes:Cartao,veiculos:MaquinaFuncionario]
	static transients =['cartaoAtivo']
	
    static constraints = {
		cpf(blank:false,cpf:true)
		matricula(blank:false,validator:{val,obj->
			
			def func=Funcionario.withCriteria{
				unidade{eq("id",obj.unidade.id)}
				eq("matricula",val)
			}
			
			if(func && func.find{it!=obj}) return "funcionario.matricula.unica" 
			
		})
		cnh(blank:false)
    }
	
	Cartao getCartaoAtivo(){
		cartoes.find{it.status==StatusCartao.ATIVO || it.status==StatusCartao.EMBOSSING}
	}
	
	Cartao cartaoAtual(){
		cartoes.max{it.dateCreated}	
	}
}
