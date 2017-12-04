package com.sysdata.gestaofrota

enum TipoLimite {
    MENSAL("Mensal"),
    DIARIO("Diário"),
    CREDITO("Crédito")

    String nome

    TipoLimite(nome) {
        this.nome = nome
    }

    String getKey() {
        name()
    }
}
