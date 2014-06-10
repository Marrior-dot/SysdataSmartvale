package com.sysdata.gestaofrota

import java.util.Date;

class MaquinaFuncionario {

	static belongsTo=[maquina:MaquinaMotorizada,funcionario:Funcionario]
	
	Date dateCreated
	Status status=Status.ATIVO

	
    static mapping={
		id generator:"sequence",params:[sequence:"maqfunc_seq"]
    }
}
