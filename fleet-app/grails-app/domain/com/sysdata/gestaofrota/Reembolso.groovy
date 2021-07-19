package com.sysdata.gestaofrota

class Reembolso {

    static belongsTo = [participante: Participante]

    static constraints = {
    }

    static mapping = {
        id generator: "sequence", params: [sequence: 'reembolso_seq']
    }
}
