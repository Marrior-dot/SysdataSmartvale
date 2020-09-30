package com.sysdata.gestaofrota


enum StatusGeracaoBoleto {

    NAO_GERAR("Não gerar boleto"),
    GERAR("Gerar boleto"),
    GERADO("Boleto gerado"),
    REGERAR("Regerar boleto")

    String nome

    StatusGeracaoBoleto(nome) {
        this.nome = nome
    }
}