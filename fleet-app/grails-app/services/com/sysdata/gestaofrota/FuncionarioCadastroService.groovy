package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class FuncionarioCadastroService {

    private def withParams(pars, clo) {
        def criteria = {
            if (pars.nome) {
                ilike("nome", pars.nome + '%')
            }
            if (pars.cpf) {
                eq("cpf", pars.cpf)
            }
        }
        clo.call(criteria)
    }

    def list(pars, paginate = true) {
        withParams(pars) { criteria ->
            return Funcionario.createCriteria().list(pars, criteria)
        }
    }

    def count(pars) {
        withParams(pars) { criteria ->
            return Funcionario.createCriteria().count(criteria)
        }
    }
}
