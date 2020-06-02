package com.sysdata.gestaofrota

class Administradora extends Participante {

    String bin
    Integer qtdCartoes = 0

    static hasMany = [role: Role]
    static constraints = {
        bin nullable: true
        qtdCartoes nullable: false, min: 0
    }
}