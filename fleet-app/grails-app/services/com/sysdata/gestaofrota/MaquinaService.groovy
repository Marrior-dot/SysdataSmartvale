package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class MaquinaService {

    private def withParams(pars, clo) {
        def criteria = {
            if (pars.placa)
                eq("placa", pars.placa)
            if (pars.codigoEquipamento)
                eq("codigo", pars.codigoEquipamento)
        }
        clo(criteria)
    }

    def list(pars, paginate = true) {
        withParams(pars) { criteria ->
            return MaquinaMotorizada.createCriteria().list(pars, criteria)
        }
    }

    def count(pars) {
        withParams(pars) { criteria ->
            return MaquinaMotorizada.createCriteria().count(criteria)
        }
    }
}
