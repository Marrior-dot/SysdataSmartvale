package com.sysdata.gestaofrota.integracao.omie

import com.sysdata.gestaofrota.Rh

class OmieCliente {

    Date dateCreated
    Rh empresaCliente
    String codigoIntegracao

    static constraints = {
        codigoIntegracao unique: true
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'omiecliente_seq']
    }

}
