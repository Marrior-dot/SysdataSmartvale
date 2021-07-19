package com.sysdata.gestaofrota

class ChaveAcessoApi {

    Date dateCreated
    String token
    Date dataHoraCriacao
    Date dataHoraExpiracao
    StatusChaveAcesso status = StatusChaveAcesso.VALIDA
    TipoAplicacao tipoAplicacao


    static constraints = {
    }

    static mapping = {
        id generator: "sequence", params: [sequence: "chaveacesso_seq"]
        token type: "text"
    }
}
