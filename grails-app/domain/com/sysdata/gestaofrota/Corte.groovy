package com.sysdata.gestaofrota

class Corte {

    Date dateCreated
    Date dataPrevista
    Date dataFechamento
    Date dataCobranca
    Date dataInicioCiclo
    StatusCorte status

    static belongsTo = [fechamento: Fechamento]

    static constraints = {
    }

    /**
     * Fatura todas as contas individuais (portadores)
     */
    void faturar(dataProc){

        def contasId=LancamentoPortador.withCriteria{
            projections{
                distinct("conta.id")
            }
            createAlias("conta","c")
            eq("statusFaturamento",StatusFaturamento.NAO_FATURADO)
            eq("corte",this)
            order("conta.id")
        }

        if(contasId.isEmpty()) log.info "Nao ha contas a faturar para este corte"
        else log.info "Total de Contas a Faturar: ${contasId.size()}"

        def totalGeral=0.0

        contasId.each{
            Conta conta=Conta.get(it)
            Fatura fatura=conta.portador.faturar(this,dataProc)
            totalGeral+=fatura.valorTotal
        }


    }

}
