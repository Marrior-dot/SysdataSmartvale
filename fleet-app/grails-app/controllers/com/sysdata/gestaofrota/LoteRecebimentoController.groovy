package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.proc.reembolso.DesfaturamentoRecebimentoConveniosService

class LoteRecebimentoController {

    DesfaturamentoRecebimentoConveniosService desfaturamentoRecebimentoConveniosService

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


    def undo(Long id) {
        LoteRecebimento loteRecebimento = LoteRecebimento.get(id)
        try {
            desfaturamentoRecebimentoConveniosService.undoBatch(loteRecebimento)
            flash.success = "Lote de Recebimento desfeito com sucesso"
        } catch (e) {
            e.printStackTrace()
            flash.error = "Erro ao desfazer Lote de Recebimento. Contate suporte para detalhes."
        }
        redirect action: 'index'
    }

}
