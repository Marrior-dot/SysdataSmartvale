package com.sysdata.gestaofrota

class CicloCobrancaService {

/**
 * Será cobrado Taxa de Utilização para qualquer cartão
 * que tenha realizado transação de Compra de Combustível e Serviço
 * no mês anterior
  */

    private def cobrarTaxaUtilizacao(f){

        def cicloAberto=CicloCobranca.cicloAberto

        def lcnPend=Lancamento.withCriteria {
                        eq("tipo",TipoLancamento.TAXA_UTILIZACAO)
                        eq("status",StatusLancamento.A_EFETIVAR)
                        eq("conta",f.conta)
                    }

        if(lcnPend) {

            //Se lançamento a efetivar com ciclo diferente do ciclo aberto, altera a referencia para este
            if (lcnPend.referencia != cicloAberto.referencia) {
                lcnPend.referencia = cicloAberto.referencia
                lcnPend.save(flush: true)
            }
        }else{

            def trCount=Transacao.withCriteria {
                "inList"("tipo",[TipoTransacao.COMBUSTIVEL,TipoTransacao.SERVICOS])
                "inList"("status",[StatusControleAutorizacao.PENDENTE,StatusControleAutorizacao.CONFIRMADA])
                eq("participante",f)


                projections {
                    rowCount("id")
                }
            }


        }




    }

    private def cobrarMensalidade(){

    }

    private def cobrarEmissaoCartao(){

    }

    private def cobrarReemissaoCartao(){

    }


    private def calcularIntervaloReferencia(dataRef){
        def dt=dataRef.clearTime()
        def mes=dataRef[Calendar.MONTH]
        def ano=dataRef[Calendar.YEAR]
        if(mes-1<1){
            ano--
            mes=12
        }else mes--
        //Primeira data intervalo
        dt.set(year:ano,month:mes,date:1)
        def ultDiaMes=dt.toCalendar().getActualMaximum(Calendar.DAY_OF_MONTH)
        def ultDia=dt.toCalendar().time
        //Ultima data intervalo
        ultDia.set(year:ano,month:mes,date:ultDiaMes)
        [prmDia:dt,ultDia:ultDia]
    }

    def execute(dateRef) {




        Rh.list().each{r->

        }
    }
}
