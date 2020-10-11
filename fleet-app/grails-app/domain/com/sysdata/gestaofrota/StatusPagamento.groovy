package com.sysdata.gestaofrota

enum StatusPagamento {

    AGENDADO("Agendado"),
    LIQUIDADO("Liquidado"),
    CANCELADO("Cancelado")

    String nome

    StatusPagamento(nome) {
        this.nome = nome
    }
}