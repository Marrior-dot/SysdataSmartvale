package com.sysdata.gestaofrota

class Telefone {
	
	String ddd
	String numero
	String ramal

    static constraints = {
		ramal nullable: true
    }

	@Override
	String toString() {
		return "(${ddd}) ${numero}"
	}
}
