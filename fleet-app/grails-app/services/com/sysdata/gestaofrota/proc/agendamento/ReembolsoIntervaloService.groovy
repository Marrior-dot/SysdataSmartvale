package com.sysdata.gestaofrota.proc.agendamento

import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.PostoCombustivel
import grails.gorm.transactions.Transactional

@Transactional
class ReembolsoIntervaloService implements CalculoDiasUteis {

    def calcularDataReembolso(PostoCombustivel empresa, Date dataReferencia) {

        def dia = dataReferencia[Calendar.DAY_OF_MONTH]
        def mes = dataReferencia[Calendar.MONTH]
        def ano = dataReferencia[Calendar.YEAR]

        // Encontra em que intervalo a data se encaixa
        def reembInstance = empresa.reembolsos.find { r -> dia >= r.inicioIntervalo && dia <= r.fimIntervalo }
        Date dataReembolso
        if (reembInstance) {
            def diaReemb = reembInstance.diaEfetivacao
            def mesReemb = mes + reembInstance.meses
            def anoReemb = ano

            if (mesReemb + 1 > 12) {
                mesReemb = 0
                anoReemb = ano + 1
            }

            dataReembolso = new Date()
            dataReembolso.set([dayOfMonth: diaReemb, month: mesReemb, year: anoReemb])
            dataReembolso = dataUtil(dataReembolso)
        } else
            throw new RuntimeException("EMP #${empresa.id} => Intervalo de Agenda não encontrado (dt: ${dataReferencia.format('dd/MM/yy')})")
        return dataReembolso.clearTime()
    }

    Date calcularProximaDataReembolso(PostoCombustivel empresa, Date dataReferencia) {

        def diaRef = dataReferencia[Calendar.DAY_OF_MONTH]
        def mesRef = dataReferencia[Calendar.MONTH] + 1
        def anoRef = dataReferencia[Calendar.YEAR]

        def proximasDatas = empresa.reembolsos.collect {
                                def dataReemb = new Date().clearTime()
                                if (it.diaEfetivacao >= diaRef)
                                    dataReemb.set([dayOfMonth: it.diaEfetivacao, month: dataReferencia[Calendar.MONTH], year: anoRef])
                                else
                                    dataReemb.set([dayOfMonth: it.diaEfetivacao, month: mesRef, year: anoRef])
                                return dataReemb
                            }
        return proximasDatas.sort().find { it > dataReferencia }

    }
}
