package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.BusinessException

class SolicitacaoCartaoProvisorioController {

    SolicitacaoCartaoProvisorioService solicitacaoCartaoProvisorioService

    def index() {
        params.max = params.max ? params.max as int : 10
        params.sort = "dateCreated"
        params.order = "desc"
        [
            solicitacaoList: solicitacaoCartaoProvisorioService.list(params),
            solicitacaoCount: solicitacaoCartaoProvisorioService.count(params)
        ]
    }

    def create() {
        respond new SolicitacaoCartaoProvisorio()
    }

    def save() {
        try {
            SolicitacaoCartaoProvisorio solicitacaoCartaoProvisorio = new SolicitacaoCartaoProvisorio(params)
            solicitacaoCartaoProvisorioService.save(solicitacaoCartaoProvisorio)
            log.info "Solicitação #${solicitacaoCartaoProvisorio.id} salva"
            flash.success = "Solicitação #${solicitacaoCartaoProvisorio.id} salva"
            redirect action: 'show', id: solicitacaoCartaoProvisorio.id
        } catch (BusinessException be) {
            flash.error = be.message
            redirect action: 'create', params: params
        } catch (Exception e) {
            e.printStackTrace()
            flash.error = "Erro interno. Contatar suporte"
        }
    }

    def show(Long id) {
        SolicitacaoCartaoProvisorio solicitacaoCartaoProvisorio = SolicitacaoCartaoProvisorio.get(id)
        respond solicitacaoCartaoProvisorio
    }

    def cancel(Long id) {
        SolicitacaoCartaoProvisorio solicitacaoCartaoProvisorio = SolicitacaoCartaoProvisorio.get(id)
        solicitacaoCartaoProvisorioService.cancel(solicitacaoCartaoProvisorio)
        log.info "Solicitação #${solicitacaoCartaoProvisorio.id} cancelada"
        flash.success = "Solicitação #${solicitacaoCartaoProvisorio.id} Cancelada"
        redirect action: 'show', id: solicitacaoCartaoProvisorio.id
    }
}
