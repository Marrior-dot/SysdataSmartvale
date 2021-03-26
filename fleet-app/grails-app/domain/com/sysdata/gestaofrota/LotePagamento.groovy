package com.sysdata.gestaofrota

/**
 * Representa o lote com todos os pagamentos de ECs programados para determinada data.
 */

class LotePagamento {

    Date dateCreated
    Date dataEfetivacao
    StatusLotePagamento status = StatusLotePagamento.ABERTO
    StatusEmissao statusEmissao = StatusEmissao.NAO_GERAR
    StatusRetornoPagamento statusRetorno

    static hasMany = [pagamentos: PagamentoLote, cortes: CorteEstabelecimento]
    
    static constraints = {
        statusRetorno nullable: true
        dataEfetivacao nullable: true
    }

    static mapping = {
        id generator: "sequence", params: ["sequence": "lotepag_seq"]
    }

    static LotePagamento getAberto() {
        LotePagamento.findByStatus(StatusLotePagamento.ABERTO)
    }

    BigDecimal getTotal() {
        return this.pagamentos ? this.pagamentos.sum { it.valor } : 0
    }
}
