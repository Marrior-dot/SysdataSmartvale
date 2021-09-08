package com.sysdata.gestaofrota.proc.agendamento

import com.sysdata.commons.dates.HolidayService

trait CalculoDiasUteis {

    HolidayService holidayService

    //TODO: Melhorar tratamento para dias úteis (feriados)
    Date dataUtil(Date data) {
        int diaSemana = data[Calendar.DAY_OF_WEEK]
        if (diaSemana == Calendar.SATURDAY)
            data += 2
        else if (diaSemana == Calendar.SUNDAY)
            data += 1
        else

        data
    }

    boolean isDataUtil(Date data) {

        ! holidayService.isHoliday(data)

        return ! (data[Calendar.DAY_OF_WEEK] in [Calendar.SATURDAY, Calendar.SUNDAY])
    }



}