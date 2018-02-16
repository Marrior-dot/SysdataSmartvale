package com.sysdata.gestaofrota

class LancamentoPortador extends Lancamento {

    Corte corte


    static transients=["extrato"]

    static constraints = {
    }

    String getExtrato(){

        String desc=""
        switch (this.tipo){
            case TipoLancamento.COMPRA:
                def strPrc=""
                desc=this.transacao.estabelecimento.descricaoResumida+strPrc
                break
            default:
                desc=this.tipo.nome
                break
        }

        desc
    }


    ItemFatura faturar(){
        ItemFatura item=new ItemFatura()
        item.with{
            descricao=this.extrato
            data=this.dataEfetivacao
            valor=this.valor
            lancamento=this
        }
        item
    }


}
