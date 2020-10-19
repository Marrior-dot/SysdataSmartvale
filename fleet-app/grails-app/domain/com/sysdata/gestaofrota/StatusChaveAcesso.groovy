package com.sysdata.gestaofrota


enum StatusChaveAcesso {

    VALIDA("Válida"),
    INVALIDA("Inválida"),
    EXPIRADA("Expirada")

    String nome

    StatusChaveAcesso(nome) {
        this.nome = nome
    }
}