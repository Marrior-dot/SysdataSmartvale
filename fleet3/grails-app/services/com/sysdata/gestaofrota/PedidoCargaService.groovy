package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class PedidoCargaService {

    def springSecurityService

    def save(PedidoCarga pedidoCarga, Map params) {
        def ret = [:]
        ret.success = true

        def hoje = new Date().clearTime()
        if (! pedidoCarga.dataCarga) {
            ret.success = false
            ret.message = "Data de Carga não pode nula!"
            return ret
        }
        if (pedidoCarga.dataCarga < hoje) {
            ret.success = false
            ret.message = "Data de Carga não pode ser inferior ao dia de Hoje!"
            return ret
        }

        def funcIds = params.findAll { it.key ==~ /func_\d+/ && it.value == 'on' }.collect { it.key.split('_')[1] as long }
        if (! funcIds) {
            ret.success = false
            ret.message = "O pedido não pode criado sem funcionário(s) vinculados!"
            return ret
        }


        Unidade unidade = pedidoCarga.unidade
        pedidoCarga.usuario = springSecurityService.getCurrentUser() as User
        pedidoCarga.validade = unidade.rh.validadeCarga
        pedidoCarga.taxa = unidade.rh.taxaPedido
        pedidoCarga.total = 0D

        funcIds.each { fid ->
            Funcionario funcionario = Funcionario.get(fid)
            ItemPedido itemPedido = new ItemPedido()
            itemPedido.participante = funcionario
            def valorCarga = params.find { it.key == "valorCarga_${fid}" }
            itemPedido.valor = valorCarga ? BigDecimal.valueOf(valorCarga) : funcionario?.categoria?.valorCarga
            itemPedido.tipo = TipoItemPedido.CARGA
            pedidoCarga.addToItens(itemPedido)
            pedidoCarga.total += itemPedido.valor
        }

        // Calcular Taxa Pedido
        if (pedidoCarga.taxa > 0) {
            def totalSemTaxa = pedidoCarga.total
            def valorTaxa = (totalSemTaxa * pedidoCarga.taxa / 100)
            def totalPedido = (totalSemTaxa + valorTaxa).round(2)
            pedidoCarga.total = totalPedido
        }

/*

        //Vincula taxas de cartão a efetivar ao pedido, caso existam
        def taxasList = getTaxasACobrar(unidadeInstance)
        taxasList.each { tx ->
            def item = new ItemPedido()
            item.with {
                participante = tx.conta.participante
                valor = tx.valor
                lancamento = tx
                tipo = TipoItemPedido.TAXA
                sobra = 0
                ativo = true
                save(flush: true)
            }
            pedidoCarga.addToItens(item)
            pedidoCarga.total += item.valor
            //Marca lançamento como EFETIVADO
            tx.status = StatusLancamento.EFETIVADO
            tx.save(flush: true);
        }
*/

        if (! pedidoCarga.save(flush: true)) {
            ret.success = false
            ret.message = pedidoCarga.errors
            return
        } else
            ret.message = "Pedido de Carga #${pedidoCarga.id} criado com sucesso"
        ret
    }

    def cancelPedido(PedidoCarga pedidoCarga) {
        def ret = [success: true]
        if (pedidoCarga.status == StatusPedidoCarga.NOVO) {
            pedidoCarga.dataCancelamento = new Date()
            pedidoCarga.status = StatusPedidoCarga.CANCELADO
            pedidoCarga.save(flush: true)
            ret.message = "Pedido Carga #$pedidoCarga.id cancelado com sucesso"
            log.info "Pedido Carga #$pedidoCarga.id CANCELADO"
        } else {
            log.info "Pedido Carga #$pedidoCarga.id não pode ser cancelado. Status: $pedidoCarga.status"
            ret.success = false
            ret.message = "Pedido Carga não pode ser cancelado. Contate suporte"
        }
        ret
    }
}
