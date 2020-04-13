package com.sysdata.gestaofrota

import javax.validation.ValidationException

class FechamentoService {

    Fechamento save(Fechamento fechamento) {
        if (!fechamento.validate()) throw new ValidationException(fechamento.showErrors())
        Fechamento fechamentoExistente = Fechamento.findByAtivoAndDiaCorteAndPrograma(true, fechamento.diaCorte, fechamento.programa)
        if (fechamentoExistente) throw new ValidationException("Já existe um Fechamento para o dia de corte (${fechamento.diaCorte}) selecionado.")

        fechamento.save()
        fechamento
    }

    void delete(Fechamento fechamento) {
        //Se ja possuir cortes, DESATIVE
        if (fechamento.cortes.size() > 0) fechamento.ativo = false
        else fechamento.delete()
    }
}
