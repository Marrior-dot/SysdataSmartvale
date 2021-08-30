package com.sysdata.gestaofrota

class RelacaoCartaoPortador {

    Date dataInicio
    Date dataFim
    StatusRelacaoCartaoPortador status = StatusRelacaoCartaoPortador.ATIVA

    static belongsTo = [cartao: Cartao, portador: Portador]

    static constraints = {
        dataFim nullable: true
    }
}
