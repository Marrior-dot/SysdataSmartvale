package com.sysdata.gestaofrota

class CorteConvenio extends Corte {

    static hasMany = [recebimentos: RecebimentoConvenio]

    static constraints = {
    }
}
