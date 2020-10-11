package com.sysdata.gestaofrota

enum StatusReenvioPagamento {

    REENVIAR_PAGAMENTO("Reenviar Pagamento"),
    INCLUIDO_LOTE("Incluído no prox. Lote")

    String nome

    StatusReenvioPagamento(nome) {
        this.nome = nome
    }

}