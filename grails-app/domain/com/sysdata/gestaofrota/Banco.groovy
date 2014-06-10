package com.sysdata.gestaofrota

class Banco {
	
	String codigo
	String nome

    static constraints = {
    }
	
	static mapping={
		id genenetor:'sequence',params:[sequence:'banco_seq']
	}
}
