package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class LotePagamentoService {

    def confirm(LotePagamento lotePagamento) {
        if (lotePagamento.statusEmissao == StatusEmissao.NAO_GERAR && lotePagamento.status == StatusLotePagamento.FECHADO) {
            lotePagamento.statusEmissao = StatusEmissao.GERAR_ARQUIVO
            lotePagamento.save(flush: true)
        } else
            throw new RuntimeException("Confirmação de Lote não permitada! Lote #${lotePagamento.id} com status inválido (${lotePagamento.statusEmissao.nome}|${lotePagamento.status.nome})!")
    }


    def getLancamentosByPagamentoEstab(PagamentoEstabelecimento pagamentoEstabelecimento, pars) {
        def criteria = {
            eq("pagamento", pagamentoEstabelecimento)
        }
        def ret = [:]
        ret.list = LancamentoEstabelecimento.createCriteria().list(pars, criteria)
        ret.count = LancamentoEstabelecimento.createCriteria().count(criteria)
        return ret
    }

    def updateDataBank(PagamentoLote pagamentoLote) {
        DadoBancario dadoBancarioAtual = pagamentoLote.estabelecimento.dadoBancario
        pagamentoLote.dadoBancario = dadoBancarioAtual
        pagamentoLote.save(flush: true)
    }

    def cancel(LotePagamento lotePagamento) {
        log.info "Cancelando Lote Repasse #$lotePagamento.id ..."
        if (lotePagamento.status != StatusLotePagamento.CANCELADO) {
            lotePagamento.status = StatusLotePagamento.CANCELADO

            // Vincula cortes liberados ao novo lote aberto
            LotePagamento loteAberto = LotePagamento.aberto

            lotePagamento.cortes.each { Corte cor ->
                cor.loteLiquidacao = null
                cor.save()
                log.info "\t(-) COR #${cor.id} desvinculado ao Lote"
                loteAberto.addToCortes(cor)
                log.info "\t(+) COR #${cor.id} vinculado ao Lote Aberto #$loteAberto.id"
            }
            loteAberto.save(flush: true)
            lotePagamento.save(flush: true)
            log.info "Lote Repasse #${lotePagamento.id} cancelado"
        } else
            throw new RuntimeException("Operação inválida para Lote com status (${lotePagamento.status.nome})")
    }

}
