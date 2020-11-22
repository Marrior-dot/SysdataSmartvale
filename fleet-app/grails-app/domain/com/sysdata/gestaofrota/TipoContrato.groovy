package com.sysdata.gestaofrota

enum TipoContrato {

    PUBLICO("Cliente Público"),
    PRIVADO("Cliente Privado")

    String nome

    TipoContrato(nome) {
        this.nome = nome
    }

}