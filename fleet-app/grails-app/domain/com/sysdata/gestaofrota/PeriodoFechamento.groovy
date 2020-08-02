package com.sysdata.gestaofrota

enum PeriodoFechamento {

    SEMANAL("Semanal"),
    QUINZENAL("Quinzenal"),
    MENSAL("Mensal")

    String nome

    PeriodoFechamento(nome) {
        this.nome = nome
    }

}