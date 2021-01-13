package com.sysdata.gestaofrota

class LoteEmbossing {

    Date dateCreated
    User usuario
    List<Arquivo> arquivos
    List<Arquivo> arquivosRetorno
    StatusLoteEmbossing status = StatusLoteEmbossing.CRIADO

    static hasMany = [cartoes: Cartao]

    static constraints = {
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'embossing_seq']
    }
}
