package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.proc.reembolso.DesfaturamentoRepasseEstabsService

class CorteEstabelecimentoController {

    DesfaturamentoRepasseEstabsService desfaturamentoRepasseEstabsService

    def index() {
        params.max = params.max ? params.max as int : 10
        params.sort = "dateCreated"
        params.order = "desc"
        [
            corteList: CorteEstabelecimento.list(params),
            corteCount: CorteEstabelecimento.count()
        ]
    }

    def undo() {
        CorteEstabelecimento corte = CorteEstabelecimento.get(params.id as long)
        try {
            desfaturamentoRepasseEstabsService.undoCutting(corte)
            flash.message = "Desfaturamento de Corte realizado com sucesso"
        } catch (e) {
            e.printStackTrace()
            flash.error = "Erro ao desfaturar Corte. Contate suporte."
        }
        redirect action: "index"
    }
}
