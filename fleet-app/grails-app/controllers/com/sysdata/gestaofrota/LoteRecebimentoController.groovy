package com.sysdata.gestaofrota

class LoteRecebimentoController {

    def index() {
        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0
        params.sort = "dateCreated"
        params.order = "desc"
        [
            loteRecebimentoList: LoteRecebimento.list(params),
            loteRecebimentoCount: LoteRecebimento.count()
        ]
    }

    def show(Long id) {
        respond LoteRecebimento.get(id)
    }
}
