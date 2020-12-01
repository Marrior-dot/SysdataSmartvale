package com.sysdata.gestaofrota.integracao.omie

enum StatusOrdemServico {

    CRIADA("Criada"),
    FATURADA("Faturada")

    String nome

    StatusOrdemServico(nome) {
        this.nome = nome
    }
}