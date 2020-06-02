package com.sysdata.gestaofrota

class LancamentoEstabelecimento extends Lancamento {

    Date dataPrevista
    BigDecimal valorTaxa

    static constraints = {
        dataPrevista nullable: true
        valorTaxa nullable: true, scale: 6
    }
}
