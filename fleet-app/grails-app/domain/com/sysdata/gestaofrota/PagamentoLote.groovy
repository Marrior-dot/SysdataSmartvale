package com.sysdata.gestaofrota

/**
 * Representa o pagamento de determinado EC em um Lote de Pagamento
 */

class PagamentoLote {

    Date dateCreated
    Date dataPrevista
    Date dataPagamento
    BigDecimal valor
    PostoCombustivel estabelecimento
    DadoBancario dadoBancario
    StatusPagamentoLote status = StatusPagamentoLote.AGENDADO
    StatusRetornoPagamento statusRetorno

    static belongsTo = [lotePagamento: LotePagamento]

    static hasMany = [pagamentos: PagamentoEstabelecimento]

    static constraints = {
        dataPagamento nullable: true
        statusRetorno nullable: true
    }

    static mapping = {
        id generator: "sequence", params: ["sequence": "pagtolote_seq"]
        dataPrevista type: 'date'
        dataPagamento type: 'date'
    }

    static embedded = ['dadoBancario']

    String toString() {
        return "${this.estabelecimento} - PgLt #${this.id} ${this.dataPrevista.format('dd/MM/yy')} ${this.valor}"
    }
}
