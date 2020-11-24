package com.sysdata.gestaofrota

enum StatusProgramacao {

    AGENDADO("Agendado"),
    CANCELADO("Cancelado")

    String nome

    StatusProgramacao(nome) {
        this.nome = nome
    }

}