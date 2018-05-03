package com.sysdata.gestaofrota

class ItemFatura {

    Date data
    String descricao
    BigDecimal valor
    Lancamento lancamento
    BigDecimal saldo=0

    static belongsTo = [fatura:Fatura]


    static constraints = {
    }

    String toString(){
        "${this.data.format('dd/MM/yyyy')}\t${sprintf('%-60s',this.descricao)}\t${sprintf('%10.2f',this.valor)}"
    }

}
