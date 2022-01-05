package com.sysdata.gestaofrota

import grails.orm.PagedResultList
import grails.plugin.springsecurity.SpringSecurityUtils

class TransacaoService {

    static transactional = false

    /**
     * Realiza a pesquisa de transações com o filtro e paginação repassados
     * @param participante
     * @param pars
     * @param paginacao
     * @return Um PagedResultList contendo a pesquisa paginada
     */


    private def withParams(pars, Closure clos) {
        def criteria = {
            if (pars.dataInicial) gt('dateCreated', pars.dataInicial)
            if (pars.dataFinal) lt('dateCreated', pars.dataFinal)
            if (pars.numeroCartao) eq("numeroCartao", pars.numeroCartao)
            if (pars.terminal) eq("terminal", pars.terminal)
            if (pars.nsu) eq("nsu", pars.nsu)
            if (pars.tipo) eq('tipo', pars.tipo)
            if (pars.tipos) 'in'('tipo', pars.tipos)
            if (pars.statusRede)
                eq("statusControle", StatusControleAutorizacao.valueOf(pars.statusRede))
        }
        clos(criteria)
    }




    def list(Map filtro, Map paginate = null) {
        withParams(filtro) { criteria ->
            if (paginate)
                return Transacao.createCriteria().list(paginate, criteria)
            else
                return Transacao.createCriteria().list(criteria)
        }
    }

    def count(filtro) {
        withParams(filtro) { criteria ->
            return Transacao.createCriteria().count(criteria)
        }
    }


    void confirmar(List<Long> ids) {
        ids.each { confirmar(Transacao.get(it)) }
    }

    Transacao confirmar(Transacao transacao) {
        if (transacao == null) return

        if (transacao.tipo == TipoTransacao.COMBUSTIVEL || transacao.tipo == TipoTransacao.SERVICOS) {
            transacao.statusControle = StatusControleAutorizacao.CONFIRMADA
            transacao.status = StatusTransacao.AGENDAR
            log.info "TR #${transacao.id} Confirmada"
        } else
            throw new RuntimeException("Tipo de Transação indevido para Confirmação: ${transacao.tipo}")

        transacao.save(flush: true)
    }

    void desfazer(List<Long> ids) {
        ids.each { desfazer(Transacao.get(it)) }
    }

    Transacao desfazer(Transacao transacao) {
        if (transacao == null) return

        if (transacao.tipo == TipoTransacao.COMBUSTIVEL || transacao.tipo == TipoTransacao.SERVICOS) {
            transacao.statusControle = StatusControleAutorizacao.DESFEITA
            log.info "TR #${transacao.id} Desfeita"
            Portador portador = transacao.cartao.portador
            def saldoAnterior = portador.saldoTotal
            portador.saldoTotal += transacao.valor
            log.info "\tPRT #${portador.id} => SA: ${saldoAnterior} NS: ${portador.saldoTotal}"
            portador.save(flush: true)
        } else
            throw new RuntimeException("Tipo de Transação indevido para Desfazimento: ${transacao.tipo}")

        transacao.save(flush: true)
    }
}
