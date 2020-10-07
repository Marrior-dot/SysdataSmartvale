package com.sysdata.gestaofrota

/**
 * Representa o lote com todos os pagamentos de ECs programados para determinada data.
 */

class LotePagamento {

    Date dateCreated
    StatusLotePagamento status = StatusLotePagamento.ABERTO
    StatusEmissao statusEmissao = StatusEmissao.NAO_GERAR

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
