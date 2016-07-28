package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.InsufficientFundsException;

class Conta {

	Double saldo=0.0
	
	static belongsTo=Participante

	static transients = ['participante']

    static constraints = {
    }
	
	static mapping={
		id generator:'sequence',params:[sequence:'conta_seq']
	}
	
	def updateSaldo(value){
		if(value>0)
			saldo+=value
		else{
			def newSaldo=saldo+value
			if(newSaldo>=0)
				saldo=newSaldo
			else
				throw new InsufficientFundsException(message:"Operação inválida. Saldo insuficiente na conta")
		}
	
	}

    Participante getParticipante(){
        Participante.findByConta(this)
    }
}
