package com.sysdata.gestaofrota

class Corte {

    Date dateCreated

    static belongsTo = [fechamento: Fechamento]

    static constraints = {
    }
}
