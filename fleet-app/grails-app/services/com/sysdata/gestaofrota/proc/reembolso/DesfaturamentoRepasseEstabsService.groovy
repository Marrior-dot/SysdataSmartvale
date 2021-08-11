package com.sysdata.gestaofrota.proc.reembolso

import com.sysdata.gestaofrota.Corte
import com.sysdata.gestaofrota.LancamentoEstabelecimento
import com.sysdata.gestaofrota.LotePagamento
import com.sysdata.gestaofrota.PagamentoEstabelecimento
import com.sysdata.gestaofrota.PagamentoLote
import com.sysdata.gestaofrota.StatusLancamento
import grails.gorm.transactions.Transactional

@Transactional
class DesfaturamentoRepasseEstabsService {

    def desfaturar(LotePagamento lotePagamento) {
        log.info "Desfazendo Lote Repasse #${lotePagamento.id}..."
        lotePagamento.pagamentos.each { PagamentoLote pagamentoLote ->
            pagamentoLote.pagamentos.each { PagamentoEstabelecimento pagamento ->
                def lancamentosList = LancamentoEstabelecimento.findAllWhere(status: StatusLancamento.FATURADO, pagamento: pagamento)
                lancamentosList.each { LancamentoEstabelecimento lancamento ->
                    lancamento.pagamento = null
                    lancamento.status = StatusLancamento.A_FATURAR
                    log.info "LC #${lancamento.id} afat"
                }
                log.info "(-) PG #${pagamento.id}"
                pagamento.delete()
            }
            log.info "(-) PGLT #${pagamentoLote.id}"
            pagamentoLote.delete()
        }
        lotePagamento.cortes.each { Corte corte ->
            log.info "(-) COR #${corte.id}"
            corte.delete()
        }
        log.info "(-) LT #${lotePagamento.id}"
        lotePagamento.delete(flush: true)
    }
}
