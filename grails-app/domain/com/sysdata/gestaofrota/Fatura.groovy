package com.sysdata.gestaofrota

class Fatura {

    Conta conta
    Corte corte
    Date data
    Date dataVencimento
    StatusFatura status

    static hasMany = [itens:ItemFatura]

    static transients = ['valorTotal']

    static constraints = {
    }

    BigDecimal getValorTotal() {
        this.itens.sum { it.valor }
    }

    String toString() {
        "FAT => #${this.id} cnt:${this.conta.id} vcto:${this.dataVencimento.format('dd/MM/yyyy')} sts:${this.status.nome} total:${Util.formatCurrency(this.valorTotal)}"
    }

}
