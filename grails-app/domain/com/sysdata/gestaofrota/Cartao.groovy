package com.sysdata.gestaofrota

class Cartao {

	String numero
	StatusCartao status=StatusCartao.CRIADO
	Date dateCreated
	Arquivo arquivo
	MotivoCancelamento motivoCancelamento
	String senha
	Date validade
	
	static belongsTo=[funcionario:Funcionario]
	
    static constraints = {
		numero(unique:true)
		motivoCancelamento(nullable:true)
		arquivo(nullable:true)
    }
	
	
	def beforeUpdate(){
		
		def histCartao
		
		if(status in [StatusCartao.ATIVO,StatusCartao.BLOQUEADO,StatusCartao.CANCELADO]){
			histCartao=new HistoricoStatusCartao(cartao:this,novoStatus:this.status)
			if(!histCartao.save())
				return false
		}
		
		return true
	}
}
