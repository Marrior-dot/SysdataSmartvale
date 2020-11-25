package com.sysdata.gestaofrota.integracao.omie

import com.sysdata.gestaofrota.Estabelecimento

class OmieCliente {

    Date dateCreated
    Estabelecimento estabelecimento
    String codigoIntegracao

    static constraints = {
        codigoIntegracao unique: true
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'omiecliente_seq']
    }

}
