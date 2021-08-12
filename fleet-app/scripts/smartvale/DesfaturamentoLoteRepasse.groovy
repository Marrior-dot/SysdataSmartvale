import com.sysdata.gestaofrota.CorteEstabelecimento

DesfaturaCorteEstabelecimento desfaturaLoteRepasse = new DesfaturaCorteEstabelecimento()
desfaturaLoteRepasse.execute(ctx.getBean('desfaturamentoRepasseEstabsService'), [4,9,13,21,24,27,29])

class DesfaturaCorteEstabelecimento {

    def execute(desfaturamentoService, corteIds) {
        corteIds.each { cid ->
            CorteEstabelecimento corteEstabelecimento = CorteEstabelecimento.get(cid)
            if (!corteEstabelecimento)
                throw new RuntimeException("Corte EC #${corteId} não encontrado!")
            desfaturamentoService.desfaturar(corteEstabelecimento)
        }
    }
}