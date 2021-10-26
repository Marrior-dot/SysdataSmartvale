package com.sysdata.gestaofrota.proc.agendamento

import com.sysdata.commons.dates.HolidayService
import com.sysdata.gestaofrota.Cidade
import com.sysdata.gestaofrota.Estado

trait CalculoDiasUteis {

    HolidayService holidayService

    Date dataUtil(Date date, Cidade cidade) {
        while (! isDataUtil(date, cidade))
            date = dataUtil(date + 1)
        return date
    }

    private boolean isWeekend(date) {
        return date[Calendar.DAY_OF_WEEK] in [Calendar.SATURDAY, Calendar.SUNDAY]
    }

    boolean isDataUtil(Date date) {
        return ! holidayService.isHoliday(date, Cidade.findWhere(nome: "BELÉM", estado: Estado.findWhere(uf: 'PA'))) && ! isWeekend(date)
    }



}