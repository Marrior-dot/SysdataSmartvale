package com.sysdata.gestaofrota

import java.util.Date;

class Veiculo extends MaquinaMotorizada {

	String placa
	MarcaVeiculo marca
	String modelo
	String chassi
	String anoFabricacao
	Long autonomia
	Date validadeExtintor
	
	
    static constraints = {
		validadeExtintor(nullable:true)
		placa unique:true,blank:false
		chassi unique:true,blank:false
		marca blank:false
		autonomia blank:false
		modelo blank:false
    }
	
	static mapping={
	}
}
