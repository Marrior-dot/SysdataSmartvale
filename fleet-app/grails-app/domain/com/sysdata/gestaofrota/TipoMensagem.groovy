package com.sysdata.gestaofrota

enum TipoMensagem {

    BANPARA_AUTENTICACAO("Autenticação (Banpará API)"),
    BANPARA_ENVIO_LOTEPAGAMENTO("Envio Lote Pagamento (Banpará API)"),
    BANPARA_ENVIO_LOTERECEBIMENTO("Envio Lote Recebimento (Banpará API)"),
    BANPARA_CONSULTA_LOTEPAGAMENTO("Consulta Lote Pagamento (Banpará API)"),
    BANPARA_CONSULTA_LOTE_DEVOLUCAO("Consulta Lotes Devolvidos (Banpará API)"),


    OMIE_CONSULTAR_CLIENTES("Consulta Clientes (Omie API)"),
    OMIE_INCLUIR_OS("Incluir Ordem Serviço (Omie API)"),
    OMIE_VALIDAR_OS("Validar Ordem Serviço (Omie API)"),

    OMIE_FATURAR_OS("Faturar Ordem Serviço (Omie API)"),
    OMIE_LISTAR_CATEGORIAS("Listar Categorias (Omie API)")


    String nome

    TipoMensagem(nome) {
        this.nome = nome
    }

}