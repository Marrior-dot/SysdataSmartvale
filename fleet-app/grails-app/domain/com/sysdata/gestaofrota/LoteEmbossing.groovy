package com.sysdata.gestaofrota

class LoteEmbossing {

    Date dateCreated
    User usuario
    List<Arquivo> arquivos
    List<ArquivoRetorno> arquivosRetorno
    StatusLoteEmbossing status = StatusLoteEmbossing.CRIADO
    TipoLoteEmbossing tipo

    static hasMany = [cartoes: Cartao]

    static constraints = {
        tipo nullable: true
        usuario nullable: true
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'embossing_seq']
    }
}
