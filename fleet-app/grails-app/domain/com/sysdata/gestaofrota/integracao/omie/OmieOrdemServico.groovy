package com.sysdata.gestaofrota.integracao.omie

import com.sysdata.gestaofrota.Fatura

class OmieOrdemServico {

    Date dateCreated
    Fatura fatura
    StatusOrdemServico status = StatusOrdemServico.CRIADA
    OmieCliente cliente

    static constraints = {
    }

    static mapping = {
        id generator: "sequence", params: [sequence: 'omieos_seq']
    }
}
