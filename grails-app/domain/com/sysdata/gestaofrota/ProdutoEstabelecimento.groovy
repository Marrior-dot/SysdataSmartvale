package com.sysdata.gestaofrota

class ProdutoEstabelecimento {
    boolean ativo = true
    BigDecimal valor = 0D
    BigDecimal valorAnterior

    static belongsTo = [estabelecimento: Estabelecimento, produto: Produto]

    static constraints = {
        ativo nullable: false
        valor nullable: false, min: 0.0
        valorAnterior nullable: true, min: 0.0
    }

    static mapping = {
        id generator: "sequence", params: [sequence: "produtoestab_seq"]
        version false
    }
}
