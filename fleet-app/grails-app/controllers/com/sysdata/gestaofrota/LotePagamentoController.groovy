package com.sysdata.gestaofrota

class LotePagamentoController {

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
}
