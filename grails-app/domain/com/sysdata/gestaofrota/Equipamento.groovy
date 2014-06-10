package com.sysdata.gestaofrota

class Equipamento extends MaquinaMotorizada {

	String codigo
	String descricao
	Long mediaConsumo
	TipoEquipamento tipo
	
    static constraints = {
		codigo blank:false,unique:true
		descricao blank:false
		mediaConsumo blank:false
		tipo blank:false
    }
	
	static mapping={
	}
}
