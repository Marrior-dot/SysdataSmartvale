package com.sysdata.gestaofrota

enum TipoMensagem {

    BANPARA_AUTENTICACAO("Autenticação (Banpará API)"),
    BANPARA_ENVIO_LOTEPAGAMENTO("Envio Lote Pagamento (Banpará API)"),
    BANPARA_CONSULTA_LOTEPAGAMENTO("Consulta Lote Pagamento (Banpará API)"),
    BANPARA_CONSULTA_LOTE_DEVOLUCAO("Consulta Lotes Devolvidos (Banpará API)")

    String nome

    TipoMensagem(nome) {
        this.nome = nome
    }

}