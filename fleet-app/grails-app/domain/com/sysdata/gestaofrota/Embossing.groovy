package com.sysdata.gestaofrota

class Embossing {

    Date dateCreated
    User usuario

    static hasMany = [cartoes: Cartao]

    static constraints = {
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'embossing_seq']
    }
}
