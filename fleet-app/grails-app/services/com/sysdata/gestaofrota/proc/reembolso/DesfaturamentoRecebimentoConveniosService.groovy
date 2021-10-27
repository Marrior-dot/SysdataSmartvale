package com.sysdata.gestaofrota.proc.reembolso

import com.sysdata.gestaofrota.CorteConvenio
import com.sysdata.gestaofrota.LoteRecebimento
import com.sysdata.gestaofrota.RecebimentoConvenio
import grails.gorm.transactions.Transactional

@Transactional
class DesfaturamentoRecebimentoConveniosService {

    def undoBatch(LoteRecebimento loteRecebimento) {
        log.info "Desfazendo Lote de Recebimento #${loteRecebimento.id}..."
        def batchIds = loteRecebimento.cortes*.id
        batchIds.each {
            CorteConvenio cutting = CorteConvenio.get(it)
            undoCutting(cutting)
        }
        loteRecebimento.delete()
        log.info "(-) LOT REC #${loteRecebimento.id}"
    }

    def undoCutting(CorteConvenio corteConvenio) {
        log.info "Desfaturando Corte Convênio #${corteConvenio.id}..."
        def receivablesIds = corteConvenio.recebimentos*.id
        receivablesIds.each {
            RecebimentoConvenio receivable = RecebimentoConvenio.get(it)
            receivable.delete()
            log.info "(-) REC #${receivable.id}"
        }
        log.info "(-) COR REC #${corteConvenio.id}"
        corteConvenio.delete()
    }

}
