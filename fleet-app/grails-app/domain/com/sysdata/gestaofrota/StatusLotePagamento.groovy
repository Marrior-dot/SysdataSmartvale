package com.sysdata.gestaofrota


enum StatusLotePagamento {

    ABERTO("Aberto"),
    FECHADO("Fechado"),
    REJEITADO("Rejeitado"),
    ACEITO("Aceito")

    String nome

    StatusLotePagamento(nome) {
        this.nome = nome
    }
}