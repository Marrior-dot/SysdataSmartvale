package com.sysdata.gestaofrota.proc

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.CartaoService
import com.sysdata.gestaofrota.SolicitacaoCartaoProvisorio
import com.sysdata.gestaofrota.StatusSolicitacaoCartaoProvisorio
import grails.gorm.transactions.Transactional

@Transactional
class ProcessadorSolicitacaoCartaoProvisorioService implements ExecutableProcessing {

    CartaoService cartaoService

    @Override
    def execute(Date date) {

        def solicitacaoList = SolicitacaoCartaoProvisorio.withCriteria {
            eq("status", StatusSolicitacaoCartaoProvisorio.CRIADA)
            order("dateCreated")
        }
        if (solicitacaoList) {
            solicitacaoList.each { SolicitacaoCartaoProvisorio sol ->
                log.info "Processando Solicitação #${sol.id}..."
                def qtdeCartoes = sol.quantidade
                qtdeCartoes.times {
                    cartaoService.gerarCartaoProvisorio()
                    if (it % 50 == 0)
                        clearSession()
                }
                if (!sol.isAttached())
                    sol.attach()
                sol.status = StatusSolicitacaoCartaoProvisorio.PROCESSADA
                sol.save(flush: true)
                log.info "Solicitação #${sol.id} processada"
            }
        } else
            log.warn "Nao há Solicitações de Cartões Provisórios a processar"
    }
}
