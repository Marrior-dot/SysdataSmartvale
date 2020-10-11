package com.sysdata.gestaofrota

/**
 * Representa cada pagamento de EC por Corte que agrupa todos os lançamentos para reembolsar em uma determinada data
 *
 */

class PagamentoEstabelecimento {

    Date dateCreated
    Date dataProgramada
    Date dataEfetivada
    PostoCombustivel estabelecimento
    CorteEstabelecimento corte
    BigDecimal valor
    StatusReenvioPagamento statusReenvio
    StatusPagamento status = StatusPagamento.AGENDADO

    static constraints = {
        dataEfetivada nullable: true
        valor nullable: true
        statusReenvio nullable: true
    }

    static mapping = {
        id generator: 'sequence', params: ['sequence': 'pagto_seq']
        dataProgramada type: 'date'
        dataEfetivada type: 'date'
    }

    String toString() {
        return "#${this.id} ${this.dataProgramada.format('dd/MM/yy')} ${this.valor}"
    }

}
