package com.sysdata.gestaofrota

class SolicitacaoCartaoProvisorio {

    Date dateCreated
    User solicitante
    Integer quantidade
    StatusSolicitacaoCartaoProvisorio status

    static constraints = {
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'soliccrtprov_seq']
    }

}
