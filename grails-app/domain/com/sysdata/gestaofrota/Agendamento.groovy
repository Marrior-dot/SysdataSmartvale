package com.sysdata.gestaofrota

class Agendamento {

    Integer diaCorte
    Integer diaVencimento

    static belongsTo = [programa: Rh]

    static constraints = {
        diaCorte nullable: false, min: 1, max: 31
        diaVencimento nullable: false, min: 1, max: 31
    }
}
