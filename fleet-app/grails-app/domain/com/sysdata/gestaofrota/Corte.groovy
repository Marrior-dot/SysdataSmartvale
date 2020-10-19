package com.sysdata.gestaofrota

class Corte {

    Date dateCreated
    Date dataPrevista
    Date dataFechamento
    Date dataCobranca
    StatusCorte status
    Boolean liberado=false

    static constraints = {
        dataFechamento nullable: true
        liberado nullable:true
    }

    static mapping = {
        id generator: "sequence", params: [sequence: "corte_seq"]
    }


    String toString(){
        "#${this.id} dt.prev:${this.dataPrevista?.format('dd/MM/yyyy')} dt.cob:${this.dataCobranca?.format('dd/MM/yyyy')}"
    }



}
