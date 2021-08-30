package com.sysdata.gestaofrota


enum StatusResetSenhaCartao {

    RESET_SENHA("Reset registrado"),
    ENVIAR_SENHA("Enviar Senha"),
    SENHA_ENVIADA("Senha enviada"),
    CANCELADA("Cancelada")

    String nome

    StatusResetSenhaCartao(nome) {
        this.nome = nome
    }
}