package com.sysdata.gestaofrota

import grails.converters.JSON
import grails.plugins.springsecurity.Secured

class EnderecoController {

    //@Secured(['IS_AUTHENTICATED_FULLY'])
    def filtrarCidadesPorEstado() {
        if(params?.cidade) params.cidade = Util.replaceSpecialChars(params.cidade.toString())
        println("PARAMS: ${params}")
        Estado estado = Estado.get(params.long('estado')) ?: Estado.findByUf(params.estado.toString())
        def cidadeList = estado.getCidades()
        Cidade cidade = cidadeList.find { it.nome.equalsIgnoreCase(params.cidade.toString()) }

        def map = [
                'estadoSelecionado' : ['id': estado.id, 'nome': estado?.nome],
                'cidadeSelecionado' : ['id': cidade?.id, 'nome': cidade?.nome],
                'cidadesDisponiveis': cidadeList.sort { it.nome }.collect { ['id': it.id, 'nome': it?.nome] }
        ]

        render map as JSON
    }


}
