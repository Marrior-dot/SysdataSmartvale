package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.InsufficientFundsException;

class Conta {
    Double saldo = 0.0

    static belongsTo = [participante: Participante, portador: Portador]

    static transients = ['participante', 'ultimaFatura']

    static constraints = {
        participante nullable: true
        portador nullable: true
    }
    static mapping = {
        id generator: 'sequence', params: [sequence: 'conta_seq']
    }

    def updateSaldo(value) {
        if (value > 0)
            saldo += value
        else {
            def newSaldo = saldo + value
            if (newSaldo >= 0)
                saldo = newSaldo
            else
                throw new InsufficientFundsException(message: "Operação inválida. Saldo insuficiente na conta")
        }

    }

    Participante getParticipante() {
        Participante.findByConta(this)
    }

    Fatura getUltimaFatura() {
        def ultFat = Fatura.withCriteria(uniqueResult: true) {
            eq("conta", this)
            eq("status", StatusFatura.ABERTA)
            firstResult(1)
            order("data", "desc")
        }
        ultFat
    }

}
