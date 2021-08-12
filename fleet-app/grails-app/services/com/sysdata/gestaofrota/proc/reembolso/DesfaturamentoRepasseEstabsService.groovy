package com.sysdata.gestaofrota.proc.reembolso

import com.sysdata.gestaofrota.CorteEstabelecimento
import com.sysdata.gestaofrota.LancamentoEstabelecimento
import com.sysdata.gestaofrota.PagamentoEstabelecimento
import com.sysdata.gestaofrota.StatusLancamento
import grails.gorm.transactions.Transactional

@Transactional
class DesfaturamentoRepasseEstabsService {

    def desfaturar(CorteEstabelecimento corteEstab) {
        log.info "Desfaturando Corte #${corteEstab.id}..."
        corteEstab.pagamentos.each { PagamentoEstabelecimento pagamento ->
            def lancamentosList = LancamentoEstabelecimento.findAllWhere(status: StatusLancamento.FATURADO, pagamento: pagamento)
            lancamentosList.each { LancamentoEstabelecimento lancamento ->
                lancamento.pagamento = null
                lancamento.status = StatusLancamento.A_FATURAR
                log.info "LC #${lancamento.id} afat"
            }
            log.info "(-) PG #${pagamento.id}"
            pagamento.delete()
        }
        log.info "(-) COR #${corteEstab.id}"
        corteEstab.delete(flush: true)
    }
}
