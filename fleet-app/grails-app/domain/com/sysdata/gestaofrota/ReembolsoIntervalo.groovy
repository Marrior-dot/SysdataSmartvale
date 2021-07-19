package com.sysdata.gestaofrota

class ReembolsoIntervalo extends Reembolso {

	Integer inicioIntervalo
	Integer fimIntervalo
	Integer diaEfetivacao
	Integer meses

	
    static constraints = {
    }

	static mapping = {
		sort "inicioIntervalo"
	}
}
