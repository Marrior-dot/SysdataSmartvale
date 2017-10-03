package com.sysdata.gestaofrota

class Telefone {
	
	String ddd
	String numero
	String ramal

    static constraints = {
    }

	@Override
	String toString() {
		return "(${ddd}) ${numero}"
	}
}
