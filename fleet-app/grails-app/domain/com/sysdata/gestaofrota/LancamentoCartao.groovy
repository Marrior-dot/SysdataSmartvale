package com.sysdata.gestaofrota

class LancamentoCartao extends Lancamento {

    Corte corte

    static transients = ["extrato"]

    static constraints = {
        corte nullable: true
    }

    String getExtrato() {

        String desc = ""
        switch (this.tipo) {
            case TipoLancamento.COMPRA:
                def strPrc = ""
                desc = this.transacao.estabelecimento.descricaoResumida + strPrc
                break
            default:
                desc = this.tipo.nome
                break
        }

        desc
    }

    static namedQueries = {
        abertosPorCorte { Corte corte ->
            eq("status", StatusLancamento.A_FATURAR)
            eq("corte", corte)
        }
    }

}
