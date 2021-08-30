package com.sysdata.gestaofrota


enum StatusRelacaoCartaoPortador {

    ATIVA("Ativa"),
    FINALIZADA_EXPIRACAO("Finalizada por expiração"),
    FINALIZADA_COMANDO("Finalizada por comando")

    String nome

    StatusRelacaoCartaoPortador(nome) {
        this.nome = nome
    }
}