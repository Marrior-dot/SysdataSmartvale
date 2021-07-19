package com.sysdata.gestaofrota

class HistoricoHodometro {

    Date dateCreated
    Veiculo veiculo
    Long hodometroAntigo
    Long hodometroNovo
    User user

    static constraints = {
        user nullable: true
    }

    static mapping={
        id generator:"sequence",params:[sequence:"histhodometro_seq"]
    }
}
