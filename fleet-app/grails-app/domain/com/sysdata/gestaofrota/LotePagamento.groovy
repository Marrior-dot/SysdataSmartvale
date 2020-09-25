package com.sysdata.gestaofrota

/**
 * Representa o lote com todos os pagamentos de ECs programados para determinada data.
 */

class LotePagamento {

    Date dateCreated
    StatusLotePagamento status = StatusLotePagamento.ABERTO
    StatusEmissaoPagamento statusEmissao = StatusEmissaoPagamento.NAO_ENVIAR

    static hasMany = [pagamentos: PagamentoLote, cortes: CorteEstabelecimento]
    
    static constraints = {
    }

    static mapping = {
        id generator: "sequence", params: ["sequence": "lotepag_seq"]
    }


    static LotePagamento getAberto() {
        LotePagamento.findByStatus(StatusLotePagamento.ABERTO)
    }
}
