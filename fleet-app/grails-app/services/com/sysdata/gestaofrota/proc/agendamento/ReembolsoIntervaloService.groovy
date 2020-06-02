package com.sysdata.gestaofrota.proc.agendamento

import com.sysdata.gestaofrota.PostoCombustivel
import grails.gorm.transactions.Transactional

@Transactional
class ReembolsoIntervaloService implements CalculoDiasUteis {

    def calcularDataReembolso(PostoCombustivel empresa, Date dataReferencia) {

        def dia = dataReferencia[Calendar.DAY_OF_MONTH]
        def mes = dataReferencia[Calendar.MONTH] + 1
        def ano = dataReferencia[Calendar.YEAR]

        // Encontra em que intervalo a data se encaixa
        def reembInstance = empresa.reembolsos.find { r -> dia >= r.inicioIntervalo && dia <= r.fimIntervalo }

        Date dataReembolso

        if (reembInstance) {
            def diaReemb = reembInstance.diaEfetivacao
            def mesReemb = mes + reembInstance.meses
            def anoReemb = ano

            if (mesReemb > 12) {
                mesReemb = 1
                anoReemb = ano + 1
            }

            dataReembolso = new Date()
            dataReembolso.set([dayOfMonth: diaReemb, month: mesReemb, year: anoReemb])
            dataReembolso = dataUtil(dataReembolso)
        }
        dataReembolso

    }
}
