package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class PedidoCargaService {

    def springSecurityService

    def save(PedidoCarga pedidoCarga, Map params) {

        Rh rh = pedidoCarga.unidade.rh
        rh.lock() // Pessimistic Locking

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

        Unidade unidade = pedidoCarga.unidade
        pedidoCarga.usuario = springSecurityService.getCurrentUser() as User
        pedidoCarga.validade = unidade.rh.validadeCarga

        if (params.tipoTaxa == '1')
            pedidoCarga.taxa = unidade.rh.taxaAdministracao
        else if (params.tipoTaxa == '2')
            pedidoCarga.taxaDesconto = unidade.rh.taxaDesconto

        pedidoCarga.total = 0D

        if (pedidoCarga.unidade.rh.vinculoCartao == TipoVinculoCartao.FUNCIONARIO) {

            def funcIds = params.findAll { it.key ==~ /func_\d+/ && it.value == 'on' }.collect { it.key.split('_')[1] as long }
            if (! funcIds) {
                ret.success = false
                ret.message = "O pedido não pode criado sem funcionário(s) vinculados!"
                return ret
            }

            funcIds.findAll { fid ->
                Funcionario funcionario = Funcionario.get(fid)
                ItemPedidoParticipante itemPedido = new ItemPedidoParticipante()
                itemPedido.participante = funcionario
                def map = params.find { it.key == "valorCarga_${fid}" }
                if (map) {
                    def valorCarga = Util.parseCurrency(map.value)
                    if (valorCarga <= funcionario.categoria.valorCarga) {
                        itemPedido.valor = valorCarga
                        itemPedido.tipo = TipoItemPedido.CARGA
                        pedidoCarga.addToItens(itemPedido)
                        pedidoCarga.total += itemPedido.valor
                    } else {
                        ret.success = false
                        ret.message = "Valor ($valorCarga) não pode ser maior que perfil de recarga (${funcionario.categoria.valorCarga})"
                        return true
                    }
                }
            }

            if (!ret.success)
                return ret


        } else if (pedidoCarga.unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA) {

            def veicIds = params.findAll { it.key ==~ /veic_\d+/ && it.value == 'on' }.collect { it.key.split('_')[1] as long }
            if (! veicIds) {
                ret.success = false
                ret.message = "O pedido não pode criado sem veículo(s) vinculados!"
                return ret
            }

            veicIds.each { fid ->
                Veiculo veiculo = Veiculo.get(fid)
                ItemPedidoMaquina itemPedido = new ItemPedidoMaquina()
                itemPedido.maquina = veiculo
                def map = params.find { it.key == "valorCarga_${fid}" }

                if (map) {
                    def valorCarga = Util.parseCurrency(map.value)
                    if (valorCarga <= veiculo.categoria.valorCarga) {
                        itemPedido.valor = valorCarga
                        itemPedido.tipo = TipoItemPedido.CARGA
                        pedidoCarga.addToItens(itemPedido)
                        pedidoCarga.total += itemPedido.valor
                    } else {
                        ret.success = false
                        ret.message = "Valor ($valorCarga) não pode ser maior que perfil de recarga (${veiculo.categoria.valorCarga})"
                        return true
                    }
                }
            }

            if (!ret.success)
                return ret
        }

        // Calcular Taxa Administração
        if (pedidoCarga.taxa > 0) {
            def totalSemTaxa = pedidoCarga.total
            def valorTaxa = (totalSemTaxa * pedidoCarga.taxa / 100)
            def totalPedido = (totalSemTaxa + valorTaxa).round(2)
            pedidoCarga.total = totalPedido

        // Calcular Taxa Desconto
        } else if (pedidoCarga.taxaDesconto > 0) {
            def totalSemTaxa = pedidoCarga.total
            def valorTaxa = (totalSemTaxa * pedidoCarga.taxaDesconto / 100)
            def totalPedido = (totalSemTaxa - valorTaxa).round(2)
            pedidoCarga.total = totalPedido
        }


        if (pedidoCarga.total > rh.saldoDisponivel) {
            ret.success = false
            ret.message = "Total do Pedido é Superior ao Limite Disponível para o Cliente"
            return ret
        } else
            rh.saldoDisponivel -= pedidoCarga.total


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

            rh.save(flush: true)

            ret.success = false
            ret.message = pedidoCarga.errors
            return
        } else
            ret.message = "Pedido #${pedidoCarga.id} criado com sucesso"
        ret
    }

    def cancelPedido(PedidoCarga pedidoCarga) {
        def ret = [success: true]
        if (pedidoCarga.status in [StatusPedidoCarga.NOVO, StatusPedidoCarga.AGENDADO]) {
            pedidoCarga.dataCancelamento = new Date()
            pedidoCarga.status = StatusPedidoCarga.CANCELADO
            pedidoCarga.save(flush: true)
            ret.message = "Pedido Carga #$pedidoCarga.id cancelado com sucesso"
            log.info "Pedido Carga #$pedidoCarga.id CANCELADO"

            // Cancela transações e lançamentos relacionadoos
            if (pedidoCarga.status == StatusPedidoCarga.AGENDADO) {

                pedidoCarga.itens.each { item ->

                    Transacao transacaoCarga = item.transacao
                    transacaoCarga.status = StatusTransacao.CANCELADA
                    transacaoCarga.save()
                    log.info "\tTR #${transacaoCarga} CANCELADA"

                    transacaoCarga.lancamentos.each { lcto ->
                        lcto.status = StatusLancamento.ESTORNADO
                        lcto.save(flush: true)
                        log.info "\t\tLC #${cto.id} ESTORNADO"
                    }
                }
            }

        } else {
            log.info "Pedido Carga #$pedidoCarga.id não pode ser cancelado. Status: $pedidoCarga.status"
            ret.success = false
            ret.message = "Pedido Carga não pode ser cancelado. Contate suporte"
        }
        ret
    }

    def releasePedido(PedidoCarga pedidoCarga) {

        def ret = [:]
        if (pedidoCarga.status == StatusPedidoCarga.COBRANCA) {

            pedidoCarga.status = StatusPedidoCarga.LIBERADO
            //Registra o usuário que realizou a liberação
            pedidoCarga.usuario = springSecurityService.currentUser

            log.info "Liberando saldo nos cartões ..."
            pedidoCarga.itens.each { item ->

                Portador portador = item.portador

                def oldSaldo = portador.saldoTotal
                portador.saldoTotal += item.valor
                portador.save(flush: true)
                log.info "\tPRT #$portador.id - (SA: $oldSaldo NS: $portador.saldoTotal)"

            }

            ret.success = true
            ret.message = "Pedido Carga #${pedidoCarga.id} LIBERADO"
            log.info ret.message
        } else {
            ret.success = false
            ret.message = "Pedido Carga #${pedidoCarga.id} não pode ser liberado. Status inválido: ${pedidoCarga.status.nome}"
            log.warn ret.message
        }
        return ret
    }

}
