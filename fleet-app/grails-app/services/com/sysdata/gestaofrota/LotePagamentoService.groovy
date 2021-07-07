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

}
