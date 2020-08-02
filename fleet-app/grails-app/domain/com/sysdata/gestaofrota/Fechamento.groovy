package com.sysdata.gestaofrota

class Fechamento {

    boolean ativo = true
    Date dateCreated
    Integer diaCorte
    Integer diasAteVencimento


    static belongsTo = [programa: Rh]

    static hasMany = [cortes: Corte]

    static constraints = {
        ativo nullable: false
        diaCorte nullable: false, min: 1, max: 31
        diasAteVencimento nullable: false, min: 1, max: 30
    }

    static namedQueries = {
        ativos {
            eq('ativo', true)
            order("diaCorte")
        }

        ativosPorPrograma { Rh programa ->
            ativos()
            eq('programa', programa)
        }
    }
}
