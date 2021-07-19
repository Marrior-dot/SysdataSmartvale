package com.sysdata.gestaofrota.integracao.omie

import com.sysdata.gestaofrota.Fatura

class OmieOrdemServico {

    Date dateCreated
    Fatura fatura
    StatusOrdemServico statusInterno = StatusOrdemServico.CRIADA
    OmieCliente cliente

    Long codigoOs
    String numeroOs
    OmieStatusOrdemServico statusOs

    static constraints = {
    }

    static mapping = {
        id generator: "sequence", params: [sequence: 'omieos_seq']
    }
}
