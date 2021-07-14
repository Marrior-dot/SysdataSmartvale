package com.sysdata.gestaofrota


enum StatusLotePagamento {

    ABERTO("Aberto"),
    FECHADO("Fechado"),
    CANCELADO("Cancelado"),
    REJEITADO("Rejeitado"),
    ACEITO("Aceito"),
    LIQUIDADO("Liquidado"),
    LIQUIDADO_PARCIALMENTE("Liquidado Parcialmente")

    String nome

    StatusLotePagamento(nome) {
        this.nome = nome
    }
}