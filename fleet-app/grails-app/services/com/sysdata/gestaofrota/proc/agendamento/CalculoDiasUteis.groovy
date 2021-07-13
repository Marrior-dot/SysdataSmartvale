package com.sysdata.gestaofrota.proc.agendamento

trait CalculoDiasUteis {

    //TODO: Melhorar tratamento para dias úteis (feriados)
    Date dataUtil(Date data) {
        int diaSemana = data[Calendar.DAY_OF_WEEK]
        if (diaSemana == Calendar.SATURDAY)
            data += 2
        else if (diaSemana == Calendar.SUNDAY)
            data += 1
        data
    }

    boolean isDataUtil(Date data) {
        return ! (data[Calendar.DAY_OF_WEEK] in [Calendar.SATURDAY, Calendar.SUNDAY])
    }



}