package com.sysdata.gestaofrota

enum StatusLoteEmbossing {

    CRIADO("Criado"),
    ARQUIVO_GERADO("Arquivo Gerado"),
    ENVIADO_EMBOSSADORA("Enviado à Embossadora")

    String nome

    StatusLoteEmbossing(nome) {
        this.nome = nome
    }

}