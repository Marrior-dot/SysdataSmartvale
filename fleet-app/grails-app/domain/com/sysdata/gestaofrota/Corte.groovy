package com.sysdata.gestaofrota


class Corte {

    Date dateCreated
    Date dataPrevista
    Date dataFechamento
    Date dataCobranca
    Date dataInicioCiclo
    StatusCorte status
    Boolean liberado=false

    static belongsTo = [fechamento: Fechamento]

    static constraints = {
        dataFechamento nullable: true
        liberado nullable:true
    }

    String toString(){
        "#${this.id} dt.prev:${this.dataPrevista.format('dd/MM/yyyy')} dt.cob:${this.dataCobranca.format('dd/MM/yyyy')}"
    }



}
