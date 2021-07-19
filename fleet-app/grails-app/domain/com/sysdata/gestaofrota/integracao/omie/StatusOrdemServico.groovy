package com.sysdata.gestaofrota.integracao.omie

enum StatusOrdemServico {

    CRIADA("Criada"),
    FATURADA("Faturada"),
    INVALIDA("Invalidada")

    String nome

    StatusOrdemServico(nome) {
        this.nome = nome
    }
}