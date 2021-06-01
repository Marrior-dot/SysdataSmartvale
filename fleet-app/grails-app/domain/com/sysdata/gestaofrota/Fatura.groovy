package com.sysdata.gestaofrota

class Fatura {

    Conta conta
    Corte corte
    Date data
    Date dataVencimento
    StatusFatura status
    StatusEmissao statusEmissao = StatusEmissao.NAO_GERAR
    StatusGeracaoBoleto statusGeracaoBoleto = StatusGeracaoBoleto.NAO_GERAR
    TipoFatura tipo


    static hasMany = [itens: ItemFatura, boletos: Boleto]

    static transients = ['valorTotal']

    static constraints = {
        corte nullable: true
        tipo nullable: true
    }

    static mapping = {
        id generator: "sequence", params: [sequence: "fatura_seq"]
        itens cascade: 'all-delete-orphan'
        boletos cascade: 'all-delete-orphan'
    }

    BigDecimal getValorTotal() {
        this.itens.sum { it.valor }
    }

    String toString() {
        "FAT => #${this.id} cnt:${this.conta.id} vcto:${this.dataVencimento.format('dd/MM/yyyy')} sts:${this.status.nome} total:${Util.formatCurrency(this.valorTotal)}"
    }


}
