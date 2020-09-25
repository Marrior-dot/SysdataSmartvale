package com.sysdata.gestaofrota

enum StatusEmissaoPagamento {

    NAO_ENVIAR("Não Enviar"),
    ENVIAR("Enviar"),
    ENVIADO("Enviado")

    String nome

    StatusEmissaoPagamento(nome) {
        this.nome = nome
    }

}