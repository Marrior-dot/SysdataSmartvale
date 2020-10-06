package com.sysdata.gestaofrota

enum StatusEmissao {

    NAO_GERAR("Não Enviar"),
    GERAR_ARQUIVO("Enviar"),
    ARQUIVO_GERADO("Enviado"),
    REGERAR_ARQUIVO("Reenviar")

    String nome

    StatusEmissao(nome) {
        this.nome = nome
    }

}