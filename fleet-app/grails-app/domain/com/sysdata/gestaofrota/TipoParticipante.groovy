package com.sysdata.gestaofrota

enum TipoParticipante {

    ADMINISTRADORA("Administradora"),
    EMPRESA_RH("Empresa RH"),
    CREDENCIADO("Credenciado"),
    ESTABELECIMENTO("Estabelecimento")

    String nome

    TipoParticipante(nome) {
        this.nome = nome
    }
}



