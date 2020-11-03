package com.sysdata.gestaofrota


enum StatusLotePagamento {

    ABERTO("Aberto"),
    FECHADO("Fechado"),
    REJEITADO("Rejeitado"),
    ACEITO("Aceito"),
    LIQUIDADO("Liquidado")

    String nome

    StatusLotePagamento(nome) {
        this.nome = nome
    }
}