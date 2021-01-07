package com.sysdata.gestaofrota.proc.agendamento

import com.sysdata.gestaofrota.PostoCombustivel
import com.sysdata.gestaofrota.ReembolsoDias
import grails.gorm.transactions.Transactional

@Transactional
class ReembolsoDiasService implements CalculoDiasUteis {

    def calcularDataReembolso(PostoCombustivel empresa, Date dataReferencia) {

        if (empresa.reembolsos[0].instanceOf(ReembolsoDias)) {
            ReembolsoDias reembolsoDias = empresa.reembolsos[0]
            return dataUtil(dataReferencia + reembolsoDias.diasTranscorridos)

        } else
            throw new RuntimeException("Reembolso do EC #${empresa.id} não é do tipo Dias Transcorridos!")
    }

}
