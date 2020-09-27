package com.sysdata.gestaofrota

enum StatusEmissao {

    NAO_ENVIAR("Não Enviar"),
    ENVIAR("Enviar"),
    ENVIADO("Enviado"),
    REENVIAR("Reenviar")

    String nome

    StatusEmissao(nome) {
        this.nome = nome
    }

}