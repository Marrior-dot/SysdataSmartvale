package com.sysdata.gestaofrota

enum TipoMensagem {

    BANPARA_AUTENTICACAO("Autenticação (Banpará API)"),
    BANPARA_ENVIO_LOTEPAGAMENTO("Envio Lote Pagamento (Banpará API)"),
    BANPARA_CONSULTA_LOTEPAGAMENTO("Consulta Lote Pagamento (Banpará API)"),
    BANPARA_CONSULTA_LOTE_DEVOLUCAO("Consulta Lotes Devolvidos (Banpará API)"),


    OMIE_CONSULTAR_CLIENTES("Consulta Clientes (Omie API)"),
    OMIE_INCLUIR_OS("Incluir Ordem Serviço (Omie API)")


    String nome

    TipoMensagem(nome) {
        this.nome = nome
    }

}