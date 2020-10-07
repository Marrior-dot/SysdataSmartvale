package com.sysdata.gestaofrota

enum TipoFatura {

    CONVENIO_PREPAGO("Convênio Pre-Pago"),
    PORTADOR_POSPAGO("Portador Pós-Pago"),
    CONVENIO_POSPAGO("Convênio Pós-Pago"),
    ESTABELECIMENTO("Estabelecimento")

    String nome

    TipoFatura(nome) {
        this.nome = nome
    }

}