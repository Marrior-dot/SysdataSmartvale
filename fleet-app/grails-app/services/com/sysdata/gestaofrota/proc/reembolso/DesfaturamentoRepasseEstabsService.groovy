package com.sysdata.gestaofrota.proc.reembolso

import com.sysdata.gestaofrota.CorteEstabelecimento
import com.sysdata.gestaofrota.LancamentoEstabelecimento
import com.sysdata.gestaofrota.LotePagamento
import com.sysdata.gestaofrota.PagamentoEstabelecimento
import com.sysdata.gestaofrota.PagamentoLote
import com.sysdata.gestaofrota.StatusLancamento
import grails.gorm.transactions.Transactional

@Transactional
class DesfaturamentoRepasseEstabsService {

    def undoBatch(LotePagamento lotePagamento) {
        log.info "Desfazendo Lote de Pagamento #${lotePagamento.id}..."
        def batchIds = lotePagamento.cortes*.id
        batchIds.each {
            CorteEstabelecimento cutting = CorteEstabelecimento.get(it)
            undoCutting(cutting)
        }
        lotePagamento.delete()
        log.info "(-) LOT PG #${lotePagamento.id}"
    }

    def undoCutting(CorteEstabelecimento corteEstab) {
        log.info "Desfaturando Corte #${corteEstab.id}..."
        def paymentsIds = corteEstab.pagamentos*.id
        paymentsIds.each {
            PagamentoEstabelecimento pagamento = PagamentoEstabelecimento.get(it)
            def lancamentosList = LancamentoEstabelecimento.findAllWhere(status: StatusLancamento.FATURADO, pagamento: pagamento)
            lancamentosList.each { LancamentoEstabelecimento lancamento ->
                lancamento.pagamento = null
                lancamento.status = StatusLancamento.A_FATURAR
                log.info "LC #${lancamento.id} afat"
                lancamento.save()
            }
            log.info "(-) PG #${pagamento.id}"
            PagamentoLote.executeUpdate("delete from PagamentoLote p where p.id in (select pl.id from PagamentoLote pl join pl.pagamentos as pe where pe =:pagto)", [pagto: pagamento])
            //corteEstab.removeFromPagamentos(pagamento)
            pagamento.delete()
        }
        log.info "(-) COR #${corteEstab.id}"
        corteEstab.delete()
    }
}
