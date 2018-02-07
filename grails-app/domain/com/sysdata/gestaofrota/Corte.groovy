package com.sysdata.gestaofrota

class Corte {

    Date dateCreated
    Date dataFechamento
    Date dataCobranca
    Date dataInicioCiclo
    Date dataFimCiclo
    StatusCorte status

    static belongsTo = [fechamento: Fechamento]

    static constraints = {
    }
}
