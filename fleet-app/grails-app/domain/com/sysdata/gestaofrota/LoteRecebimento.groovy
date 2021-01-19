package com.sysdata.gestaofrota

import groovy.util.logging.Slf4j

@Slf4j
class LoteRecebimento {

    Date dateCreated
    StatusLotePagamento status = StatusLotePagamento.ABERTO
    StatusEmissao statusEmissao = StatusEmissao.NAO_GERAR
    StatusRetornoPagamento statusRetorno

    static hasMany = [recebimentos: RecebimentoLote, cortes: CorteConvenio]

    static constraints = {
        statusRetorno nullable: true
    }

    static mapping = {
        id generator: "sequence", params: ["sequence": "lotereceb_seq"]
    }

    static LoteRecebimento getAberto() {
        LoteRecebimento loteAberto = LoteRecebimento.findByStatus(StatusLotePagamento.ABERTO)
        if (!loteAberto) {
            loteAberto = new LoteRecebimento()
            loteAberto.save(flush: true)
            log.info "Lote Recebimento #${loteAberto.id} criado"
        }
        return loteAberto
    }

}
