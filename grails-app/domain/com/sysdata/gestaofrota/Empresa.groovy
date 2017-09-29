package com.sysdata.gestaofrota

class Empresa extends Participante {

    String cnpj
    String nomeFantasia
    String inscricaoEstadual
    String inscricaoMunicipal

    static constraints = {
        cnpj(blank: false, unique: true)
        nomeFantasia(blank: false)
        inscricaoEstadual(nullable: true)
        inscricaoMunicipal(nullable: true)
    }
}
