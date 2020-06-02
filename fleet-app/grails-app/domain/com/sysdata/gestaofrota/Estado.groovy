package com.sysdata.gestaofrota

class Estado {
	String nome
	String uf

	static transients = ['cidades']

	static constraints = {
		nome(maxSize: 40, nullable: true)
		uf(maxSize: 2)
	}

	String toString() {
		uf
	}

	static mapping = {
		id generator: 'sequence', params: [sequence: 'estado_seq']
	}

	def getCidades(){
		Cidade.findAllByEstado(this)
	}
}
