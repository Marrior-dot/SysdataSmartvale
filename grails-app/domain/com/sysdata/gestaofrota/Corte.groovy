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
        dataFechamento nullable: true
    }

    String toString(){
        "#${this.id} dt.prev:${this.dataPrevista.format('dd/MM/yyyy')} dt.cob:${this.dataCobranca.format('dd/MM/yyyy')}"
    }


    /**
     * Fatura todas as contas individuais (portadores)
     */
    void faturar(dataProc){

/*
        def contasId=LancamentoPortador.withCriteria{
            projections{
                distinct("conta.id")
            }
            createAlias("conta","c")
            eq("statusFaturamento",StatusFaturamento.NAO_FATURADO)
            eq("corte",this)
            order("conta.id")
        }
*/

        def contasId=Portador.withCriteria {
            projections {
                property("conta.id")
            }
            unidade{
                rh{
                    fechamentos{
                        eq("id",this.fechamento.id)
                    }
                }
            }
            order("conta.id")
        }

        if(contasId.isEmpty()) log.info "Nao ha contas a faturar para este corte"
        else log.info "Total de Contas a Faturar: ${contasId.size()}"

        def totalGeral=0.0

        contasId.each{
            Conta conta=Conta.get(it)
            Fatura fatura=conta.portador.faturar(this,dataProc)

        }


    }

}
