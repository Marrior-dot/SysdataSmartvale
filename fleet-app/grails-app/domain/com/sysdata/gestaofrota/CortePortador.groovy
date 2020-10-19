package com.sysdata.gestaofrota

class CortePortador extends Corte {

    Date dataInicioCiclo

    static belongsTo = [fechamento: Fechamento]

    static constraints = {
    }
}
