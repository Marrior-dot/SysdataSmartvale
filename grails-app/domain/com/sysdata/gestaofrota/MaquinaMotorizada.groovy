package com.sysdata.gestaofrota

class MaquinaMotorizada {

	Long capacidadeTanque
	TipoAbastecimento tipoAbastecimento
	Date dateCreated

	static auditable = true
	
	static belongsTo=[unidade:Unidade]
	
	static hasMany=[funcionarios:MaquinaFuncionario]
	
	static constraints={
		dateCreated nullable:true
	}
	
    static mapping={
		id generator:'sequence',params:[sequence:'maquina_seq']
    }
}
