package com.sysdata.gestaofrota

class ProdutoEstabelecimento {
    boolean ativo = true
    Double valor = 0D
    Double valorAnterior

    static belongsTo = [estabelecimento: Estabelecimento, produto: Produto]

    static constraints = {
        ativo nullable: false
        valor nullable: false, min: 0D
        valorAnterior nullable: true, min: 0D
    }

    static mapping = {
        id generator: "sequence", params: [sequence: "produtoestab_seq"]
        version false
    }
}
