package com.sysdata.gestaofrota

class CortePortador extends Corte {

    Date dataInicioCiclo

    static belongsTo = [fechamento: Fechamento]

    static constraints = {
    }

    static namedQueries = {
        ativosPorFechamento { Fechamento fechamento ->
            eq("status", StatusCorte.ABERTO)
            eq("fechamento", fechamento)
        }
    }

}
