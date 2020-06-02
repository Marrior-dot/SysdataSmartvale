package com.sysdata.gestaofrota

enum OrigemTransacao {
    REDE("Rede Captura"),
    PORTAL("Portal Web"),
    API("API Web Service"),
    MOCK("Gerador Mock")

    String nome

    OrigemTransacao(nome) {
        this.nome = nome
    }
}