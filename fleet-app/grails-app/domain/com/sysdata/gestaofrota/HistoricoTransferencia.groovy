package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.Transacao
import com.sysdata.gestaofrota.User

class HistoricoTransferencia {

    Transacao transacaoDebito
    Transacao transacaoCredito
    BigDecimal valorTransferencia
    Date dateCreated
    User user

    static constraints = {
        user nullable: true
    }
}