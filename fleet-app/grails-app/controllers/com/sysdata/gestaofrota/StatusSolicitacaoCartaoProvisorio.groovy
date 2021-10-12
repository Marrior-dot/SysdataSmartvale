package com.sysdata.gestaofrota

enum StatusSolicitacaoCartaoProvisorio {

    CRIADA("Criada"),
    PROCESSADA("Processada"),
    CANCELADA("Cancelada")

    String nome

    StatusSolicitacaoCartaoProvisorio(nome) {
        this.nome = nome
    }

}