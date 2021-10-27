package com.sysdata.gestaofrota.proc.agendamento

import com.sysdata.gestaofrota.PostoCombustivel
import com.sysdata.gestaofrota.ReembolsoSemanal
import grails.gorm.transactions.Transactional

import java.time.DayOfWeek

@Transactional
class ReembolsoSemanalService implements CalculoDiasUteis {

    def calcularDataReembolso(PostoCombustivel empresa, Date dataReferencia) {
        ReembolsoSemanal reembolsoSemanal = empresa.reembolsos[0]
        DayOfWeek diaCorte = reembolsoSemanal.diaSemana.dayOfWeek.plus(6)
        def diaSemana = dataReferencia[Calendar.DAY_OF_WEEK]
        def delta = diaCorte.value - diaSemana
        Date dataCorte
        if (delta >= 0)
            dataCorte = dataReferencia + delta
        else
            dataCorte = dataReferencia + 6 + delta
        Date dataReembolso = dataCorte + reembolsoSemanal.intervaloDias
        dataReembolso = dataUtil(dataReembolso)
        return dataReembolso.clearTime()
    }
}
