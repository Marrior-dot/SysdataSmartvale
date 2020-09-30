package com.sysdata.gestaofrota

class MensagemIntegracao {

    Date dateCreated
    User usuario
    String corpo
    String resposta
    String codigoResposta
    Map jsonResponse
    TipoMensagem tipo

    static constraints = {
        usuario nullable: true
        resposta nullable: true
        codigoResposta nullable: true
        jsonResponse nullable: true
    }

    static mapping = {
        id generator: "sequence", params: [sequence: 'msginteg_seq']
    }
}
