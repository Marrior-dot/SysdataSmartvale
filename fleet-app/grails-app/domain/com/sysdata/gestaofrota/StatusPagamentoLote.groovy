package com.sysdata.gestaofrota

enum StatusPagamentoLote {

    AGENDADO("Agendado"),
    ACEITO("Aceito"),
    LIQUIDADO("Liquidado"),
    REJEITADO("Rejeitado")

    String nome

    StatusPagamentoLote(nome) {
        this.nome = nome
    }
}