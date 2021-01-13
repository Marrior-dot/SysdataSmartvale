package com.sysdata.gestaofrota

class ArquivoRetorno {

    Arquivo arquivo

    static constraints = {
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'arqretorno_seq']
    }

}
