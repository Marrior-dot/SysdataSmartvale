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



        Unidade unidade = pedidoCarga.unidade
        pedidoCarga.usuario = springSecurityService.getCurrentUser() as User
        pedidoCarga.validade = unidade.rh.validadeCarga

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
                MaquinaMotorizada veiculo = MaquinaMotorizada.get(fid)
                ItemPedidoMaquina itemPedido = new ItemPedidoMaquina()
                itemPedido.maquina = veiculo
                def map = params.find { it.key == "valorCarga_${fid}" }

                if (map) {
                    def valorCarga = Util.parseCurrency(map.value)
                    if (valorCarga <= veiculo.categoria.valorCarga) {
                        itemPedido.valor = valorCarga
                        itemPedido.tipo = TipoItemPedido.CARGA
                        pedidoCarga.addToItens(itemPedido)
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

        // Calcular com Taxa de Administração
        if (params.tipoTaxa == '1') {

            pedidoCarga.taxa = unidade.rh.taxaAdministracao

            if (pedidoCarga.taxa > 0) {
                def valorTaxaAdm = (pedidoCarga.totalBruto * pedidoCarga.taxa / 100).round(2)

                ItemPedidoTaxa itemPedidoTaxa = new ItemPedidoTaxa()
                itemPedidoTaxa.with {
                    tipo = TipoItemPedido.TAXA
                    valor = valorTaxaAdm
                }
                pedidoCarga.addToItens(itemPedidoTaxa)
            }

        // Calcular com Taxa de Desconto
        } else if (params.tipoTaxa == '2') {

            pedidoCarga.taxaDesconto = unidade.rh.taxaDesconto

            if (pedidoCarga.taxaDesconto > 0 ) {
                def valorTaxaDesc = (pedidoCarga.totalBruto * pedidoCarga.taxaDesconto / 100).round(2)

                ItemPedidoTaxa itemPedidoTaxa = new ItemPedidoTaxa()
                itemPedidoTaxa.with {
                    tipo = TipoItemPedido.TAXA
                    valor = -valorTaxaDesc
                }
                pedidoCarga.addToItens(itemPedidoTaxa)
            }
        }


        if (pedidoCarga.total > rh.saldoDisponivel) {
            ret.success = false
            ret.message = "Total do Pedido é Superior ao Limite Disponível para o Cliente"
            return ret
        } else
            rh.saldoDisponivel -= pedidoCarga.total

        def saved = true

        if (pedidoCarga.instanceOf(PedidoCargaInstancia)) {
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
            saved = pedidoCarga.save(flush: true)

        } else if (pedidoCarga.instanceOf(PedidoCargaProgramado)) {

            PedidoCargaProgramado pedidoProgramado = (pedidoCarga as PedidoCargaProgramado)
            pedidoProgramado.agendas = null

            // Agenda Mensal
            if (params.recorrencia == "1") {
                params.findAll { it.key ==~ /agendas\[\d+\]/}.each { k, v ->
                    if (v instanceof Map) {
                        MensalAgendaPedido agendaMensal = new MensalAgendaPedido(v)
                        pedidoProgramado.addToAgendas(agendaMensal)
                        if (! agendaMensal.validate()) {
                            ret.success = false
                            ret.message = Util.formatDomainErrors(agendaMensal)
                            return false
                        }
                    }
                }
                if (! ret.success)
                    return ret

            }
            saved = pedidoProgramado.save(flush: true)
        }

        if (! saved) {
            rh.save(flush: true)

            ret.success = false
            ret.message = pedidoCarga.errors
        } else
            ret.message = "Pedido #${pedidoCarga.id} criado com sucesso"

        return ret
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

                    Lancamento lancamento = item.lancamento
                    lancamento.status = StatusLancamento.ESTORNADO
                    lancamento.save(flush: true)
                    log.info "\tLC #${lancamento.id} ESTORNADO"

                    if (lancamento.transacao) {
                        Transacao tr = lancamento.transacao
                        tr.status = StatusTransacao.CANCELADA
                        tr.save(flush: true)
                        log.info "\tTR #${tr.id} CANCELADA"
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
            pedidoCarga.itensCarga.each { item ->
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

    def deletePedido(PedidoCarga pedidoCarga) {

        def ret = [:]

        if ((pedidoCarga.instanceOf(PedidoCargaInstancia) && pedidoCarga.status == StatusPedidoCarga.NOVO) ||
            (pedidoCarga.instanceOf(PedidoCargaProgramado) && pedidoCarga.statusProgramacao == StatusProgramacao.AGENDADO)
        ) {

            def pedId = pedidoCarga.id

            def itemIds = pedidoCarga.itens*.id
            itemIds.each { iid ->
                ItemPedido item = ItemPedido.get(iid)
                pedidoCarga.removeFromItens(item)
                item.delete(flush: true)
            }
            pedidoCarga.delete(flush: true)

            ret.success = true
            ret.message = "Pedido de Carga #${pedId} excluído com sucesso"
            log.info ret.message

            return ret
        } else {
            ret.success = false
            ret.message = "Pedido de Carga #${pedidoCarga.id} não pode ser excluído. Status: ${pedidoCarga.status.nome}"
            log.warn ret.message

            return ret
        }


    }
}
