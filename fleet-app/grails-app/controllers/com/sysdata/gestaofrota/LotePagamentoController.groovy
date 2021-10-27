package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.banpara.ConsultaLoteRepasseIndividualService
import com.sysdata.gestaofrota.http.ResponseData
import com.sysdata.gestaofrota.proc.reembolso.DesfaturamentoRepasseEstabsService

class LotePagamentoController {

    LotePagamentoService lotePagamentoService
    ConsultaLoteRepasseIndividualService consultaLoteRepasseIndividualService
    DesfaturamentoRepasseEstabsService desfaturamentoRepasseEstabsService


    def index() {
        params.max = params.max ? params.max as int: 10
        params.offset = params.offset ? params.offset as int: 0
        params.sort = "dateCreated"
        params.order = "desc"
        [lotePagamentoList: LotePagamento.list(params), lotePagamentoCount: LotePagamento.count()]
    }

    def show(Long id) {
        respond LotePagamento.get(id)
    }

    def confirm(Long id) {
        LotePagamento lotePagamento = LotePagamento.get(id)
        if (lotePagamento) {
            try {
                lotePagamentoService.confirm(lotePagamento)
                def message = "Lote #${lotePagamento.id} Confirmado p/ Envio"
                flash.success = message
                log.info message
            } catch (e) {
                flash.error = ["Erro: ${e.message}"]
                log.error e.message
            }
            lotePagamento.attach()
            render view: 'show', model: [lotePagamento: lotePagamento]
        }
    }

    def showPagamentoLote(Long id) {
        respond PagamentoLote.get(id)
    }

    def showPagamentoEstab() {
        def pagEstabId = params.pagEstabId.toLong()
        def pagLoteId = params.pagLoteId as long
        PagamentoEstabelecimento pagamentoEstabelecimento = PagamentoEstabelecimento.get(pagEstabId)
        PagamentoLote pagamentoLote = PagamentoLote.get(pagLoteId)
        [
            pagamentoEstabelecimento: pagamentoEstabelecimento,
            pagamentoLote: pagamentoLote
        ]
    }

    def updateDataBank(Long id) {
        PagamentoLote pagamentoLote = PagamentoLote.get(id)
        try {
            lotePagamentoService.updateDataBank(pagamentoLote)
            def message = "Atualizado Domicílio Bancário do EC referenciado ao Pagamento Lote #${pagamentoLote.id}"
            flash.success = message
            log.info message
        } catch (e) {
            flash.error = ["Erro: $e.message"]
            log.error e.message
        }
        pagamentoLote.attach()
        render view: 'showPagamentoLote', model: [pagamentoLote: pagamentoLote]
    }

    def loadEntries() {
        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0
        def pagEstabId = params.pagEstabId as long
        PagamentoEstabelecimento pagamentoEstabelecimento = PagamentoEstabelecimento.get(pagEstabId)
        def ret = lotePagamentoService.getLancamentosByPagamentoEstab(pagamentoEstabelecimento, params)
        render template: 'lancamentos', model: [entriesList: ret.list, entriesCount: ret.count]
    }

    def queryPayment(Long id) {
        PagamentoLote pagamentoLote = PagamentoLote.get(id)
        try {
            ResponseData responseData = consultaLoteRepasseIndividualService.queryPayment(pagamentoLote)
            render template: 'queryPayment', model: [response: responseData]
        } catch (e) {
            render template: 'queryPayment', model: [error: e.message]
        }
    }

    def cancel(Long id) {
        try {
            lotePagamentoService.cancel(LotePagamento.get(id))
            redirect action: 'show', id: id
        } catch (e) {
            redirect action: 'show', id: id
        }
    }

    def undo(Long id) {
        LotePagamento lotePagamento = LotePagamento.get(id)
        try {
            desfaturamentoRepasseEstabsService.undoBatch(lotePagamento)
            flash.success = "Lote de Pagamento desfeito com sucesso"
        } catch (e) {
            e.printStackTrace()
            flash.error = "Erro ao desfazer Lote de Pagamento. Contate suporte para detalhes."
        }
        redirect action: 'index'
    }

}
