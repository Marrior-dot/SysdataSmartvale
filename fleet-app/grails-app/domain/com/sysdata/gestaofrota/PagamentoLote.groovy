package com.sysdata.gestaofrota

/**
 * Representa cada pagamento para determinado EC por Lote de Pagamento
 */
class PagamentoLote {

    Date dateCreated
    Date dataPrevista
    Date dataPagamento
    DadoBancario dadoBancario

    static belongsTo = [lotePagamento: LotePagamento]

    static hasMany = [pagamentos: PagamentoEstabelecimento]

    static constraints = {
    }

    static mapping = {
        id generator: "sequence", params: ["sequence": "pagtolote_seq"]
        dataPrevista type: 'date'
        dataPagamento type: 'date'
    }

    static embedded = ['dadoBancario']
}
