package com.sysdata.gestaofrota

class MockTransacaoController {

    MockTransacaoService mockTransacaoService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = "id"
        params.order = "desc"
        def criteria = {
            eq("origem", OrigemTransacao.MOCK)
        }

        def transacaoList = Transacao.createCriteria().list(params, criteria)
        def transacaoCount = Transacao.createCriteria().count(criteria)

        [ transacaoList: transacaoList, transacaoCount: transacaoCount ]
    }

    def gerarTransacoes() {
        try {
            def ret = mockTransacaoService.gerarTransacoes(params)
            if (ret.success)
                flash.success = "Transação geradas com sucesso!"
            else {
                log.debug ret.messages
                flash.error = ret.messages
            }
        } catch(e) {
            e.printStackTrace()
            flash.error = e.message
        }
        redirect action: 'index'
    }

}
