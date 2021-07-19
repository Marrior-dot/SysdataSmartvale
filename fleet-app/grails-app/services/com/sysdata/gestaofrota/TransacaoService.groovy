package com.sysdata.gestaofrota

import grails.orm.PagedResultList
import grails.plugin.springsecurity.SpringSecurityUtils

class TransacaoService {

    static transactional = false

    /**
     * Realiza a pesquisa de transações com o filtro e paginação repassados
     * @param participante
     * @param filtro
     * @param paginacao
     * @return Um PagedResultList contendo a pesquisa paginada
     */
    PagedResultList pesquisar(final Participante participante, final Map filtro, final Map paginacao) {
        Unidade unidade
        Estabelecimento estabelecimento

/*
        if (SpringSecurityUtils.ifAllGranted("ROLE_ESTAB") && participante?.instanceOf(PostoCombustivel)) {
            PostoCombustivel postoCombustivel = PostoCombustivel.get(participante.id)
            estabelecimento = Estabelecimento.findByEmpresa(postoCombustivel)
        } else if (SpringSecurityUtils.ifAllGranted("ROLE_RH") && participante?.instanceOf(Rh)) {
            Rh rh = Rh.get(participante.id)
            unidade = Unidade.findByRh(rh)
        }
*/



        return Transacao.createCriteria().list(paginacao) {
            if (filtro.dataInicial) gt('dateCreated', filtro.dataInicial)
            if (filtro.dataFinal) lt('dateCreated', filtro.dataFinal)
            if (filtro.numeroCartao) eq("numeroCartao", filtro.numeroCartao)
            if (filtro.terminal) eq("terminal", filtro.terminal)
            if (filtro.nsu) eq("nsu", filtro.nsu)
            if (filtro.tipo) eq('tipo', filtro.tipo)
            if (filtro.tipos) 'in'('tipo', filtro.tipos)
            if (filtro.statusRede)
                eq("statusControle", StatusControleAutorizacao.valueOf(filtro.statusRede))

/*
            if (filtro.statusControle) {
                if (filtro.statusControle instanceof StatusControleAutorizacao)
                    eq('statusControle', filtro.statusControle)
                else eq('statusControle', StatusControleAutorizacao.valueOf(filtro.statusControle.toString()))
            }
*/

/*
            if (unidade) {
                participante {
                    eq('unidade', unidade)
                }
            }
            if (estabelecimento?.codigo?.length() > 0) {
                eq("codigoEstabelecimento", estabelecimento.codigo)
            }
*/
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
