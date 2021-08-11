import com.sysdata.gestaofrota.LotePagamento

DesfaturaLoteRepasse desfaturaLoteRepasse = new DesfaturaLoteRepasse()
desfaturaLoteRepasse.execute(ctx.getBean('desfaturamentoRepasseEstabsService'), 1)

class DesfaturaLoteRepasse {

    def execute(desfaturamentoService, lotePagtoId) {
        LotePagamento lotePagamento = LotePagamento.get(lotePagtoId)
        if (!lotePagamento)
            throw new RuntimeException("Lote Repasse #${lotePagtoId} não encontrado!")
        desfaturamentoService.desfaturar(lotePagamento)
    }
}