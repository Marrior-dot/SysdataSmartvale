package com.sysdata.gestaofrota

class ParticipanteService {

    static transactional=false

    def saveCidade(enderecoInstance) {
		def nomeCidade
		def nomeEstado

		if(enderecoInstance?.cidade){
			nomeCidade=enderecoInstance.cidade.nome
			nomeEstado=enderecoInstance.cidade.estado.nome
			
			enderecoInstance.cidade=null
			
			def cidadeInstance=Cidade.withCriteria(uniqueResult:true){
				estado{eq("nome",nomeEstado)}
				eq("nome",nomeCidade)
			}
			enderecoInstance.cidade=cidadeInstance
		}
    }
}
