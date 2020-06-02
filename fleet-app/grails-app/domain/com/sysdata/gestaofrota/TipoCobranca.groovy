package com.sysdata.gestaofrota

enum TipoCobranca {

    PRE_PAGO("Pré pago"),
    POS_PAGO("Pós Pago")

    String nome

    TipoCobranca(nome) {
        this.nome = nome
    }

    String getKey() {
        name()
    }

    static def list() {
        [TipoCobranca.PRE_PAGO]
    }
}
