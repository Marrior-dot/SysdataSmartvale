package com.sysdata.gestaofrota.proc.agendamento

trait CalculoDiasUteis {

    //TODO: Melhorar tratamento para dias úteis (feriados)
    Date dataUtil(Date data) {
        int diaSemana = data[Calendar.DAY_OF_WEEK]

        if (diaSemana == 6)
            data += 2
        else if (diaSemana == 7)
            data += 1

        data
    }
}