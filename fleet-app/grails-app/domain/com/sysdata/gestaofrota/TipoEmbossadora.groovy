package com.sysdata.gestaofrota

enum TipoEmbossadora {
    INTELCAV("Intel Cav"),
    PAYSMART("Pay Smart")

    String nome

    TipoEmbossadora(String nome) {
        this.nome = nome
    }

    String getKey() {
        return name()
    }
}
