package com.sysdata.gestaofrota

import grails.converters.JSON

class EnderecoController {

    EnderecoService enderecoService

    def filtrarCidadesPorEstado() {
        List<Cidade> cidades = []
        if (params.estId) {
            def estId = params.estId as long
            cidades = Cidade.withCriteria {
                estado {
                    eq("id", estId)
                }
            }
        }
        render cidades as JSON
    }


    /**
     *
     * Utiliza o WS viacep para procurar endereço por CEP
     */
    def findEnderecoByCep() {
        def cep = params.cep
        def end = enderecoService.findEnderecoByCep(cep)
        if (!end) {
            response.status = 404
            render text: "Endereço não encontrado para o CEP $cep"
        }
        render end

    }


}
