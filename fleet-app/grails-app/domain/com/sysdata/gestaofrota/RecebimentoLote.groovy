package com.sysdata.gestaofrota

class RecebimentoLote {

    Date dateCreated
    Date dataPrevista
    Date dataRecebimento
    Rh convenio
    DadoBancario domicilioBancario
    BigDecimal valor
    BigDecimal valorBruto
    BigDecimal valorTaxaAdm
    StatusPagamentoLote status = StatusPagamentoLote.AGENDADO
    StatusRetornoPagamento statusRetorno

    static belongsTo = [loteRecebimento: LoteRecebimento]

    static hasMany = [recebimentos: RecebimentoConvenio]

    static constraints = {
        statusRetorno nullable: true
        dataRecebimento nullable: true
        valorBruto nullable: true
        valorTaxaAdm nullable: true
    }

    static mapping = {
        id generator: "sequence", params: ["sequence": "pagtolote_seq"]
        dataPrevista type: 'date'
        dataRecebimento type: 'date'
    }

    static embedded = ['domicilioBancario']

    String toString() {
        return "#${this.id} Rh: ${this.convenio} Dt.Prv: ${dataPrevista.format('dd/MM/yy')} Val: ${Util.formatCurrency(this.valor)}"
    }

}
