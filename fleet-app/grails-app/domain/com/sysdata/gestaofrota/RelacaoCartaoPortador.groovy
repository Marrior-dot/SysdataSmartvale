package com.sysdata.gestaofrota

class RelacaoCartaoPortador {

    Date dataInicio
    Date dataFim
    StatusRelacaoCartaoPortador status = StatusRelacaoCartaoPortador.ATIVA
    EnvioResetSenha envioResetSenha = EnvioResetSenha.NAO_ENVIAR

    static belongsTo = [cartao: Cartao, portador: Portador]

    static constraints = {
        dataFim nullable: true
    }
}
