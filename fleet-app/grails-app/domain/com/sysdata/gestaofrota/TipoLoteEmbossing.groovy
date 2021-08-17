package com.sysdata.gestaofrota

enum TipoLoteEmbossing {

    PADRAO("Cartão Padrão"),
    PROVISORIO("Cartão Provisório")

    String nome

    TipoLoteEmbossing(nome) {
        this.nome = nome
    }

}