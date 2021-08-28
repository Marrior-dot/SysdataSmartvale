package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.BusinessException

class SolicitacaoCartaoProvisorioService {

    def springSecurityService

    private static final int MAX_CARTOES = 1000

    private def withParams(pars, Closure clo) {
        def criteria = {
            if (pars.status != 'null' && pars.status)
                eq("status", StatusSolicitacaoCartaoProvisorio.valueOf(pars.status))
        }
        clo(criteria)
    }

    def list(pars) {
        withParams(pars) { criteria ->
            return SolicitacaoCartaoProvisorio.createCriteria().list(pars, criteria)
        }
    }

    def count(pars) {
        withParams(pars) { criteria ->
            return SolicitacaoCartaoProvisorio.createCriteria().count(criteria)
        }
    }

    def save(SolicitacaoCartaoProvisorio solicitacaoCartaoProvisorio) {
        if (solicitacaoCartaoProvisorio.quantidade <= MAX_CARTOES) {
            User user = springSecurityService.currentUser
            solicitacaoCartaoProvisorio.solicitante = user
            solicitacaoCartaoProvisorio.status = StatusSolicitacaoCartaoProvisorio.CRIADA
            solicitacaoCartaoProvisorio.save(flush: true)
        } else
            throw new BusinessException("Quantidade solicitada inválida! Número (${solicitacaoCartaoProvisorio.quantidade}) superior ao permitido ${MAX_CARTOES}")
    }

    def cancel(SolicitacaoCartaoProvisorio solicitacaoCartaoProvisorio) {
        if (solicitacaoCartaoProvisorio.status == StatusSolicitacaoCartaoProvisorio.CRIADA) {
            solicitacaoCartaoProvisorio.status = StatusSolicitacaoCartaoProvisorio.CANCELADA
            solicitacaoCartaoProvisorio.save(flush: true)
        } else
            throw new BusinessException("Cancelamento inválido! Status da Solicitação incompatível com esta operação (${solicitacaoCartaoProvisorio.status.nome})")
    }
}
