package com.sysdata.gestaofrota

enum TipoCorteEstabelecimento {

    REEMBOLSO("Reembolso"),
    ANTECIPACAO("Antecipacao")

    String nome

    TipoCorteEstabelecimento(nome) {
        this.nome = nome
    }

}