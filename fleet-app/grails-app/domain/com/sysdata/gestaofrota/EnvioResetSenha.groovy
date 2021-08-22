package com.sysdata.gestaofrota


enum EnvioResetSenha {

    NAO_ENVIAR("Não enviar"),
    ENVIAR("Enviar nova senha"),
    ENVIADA("Senha enviada")

    String nome

    EnvioResetSenha(nome) {
        this.nome = nome
    }
}