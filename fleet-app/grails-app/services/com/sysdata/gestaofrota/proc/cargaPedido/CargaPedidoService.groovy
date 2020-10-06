package com.sysdata.gestaofrota.proc.cargaPedido

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import grails.gorm.transactions.Transactional
import org.springframework.transaction.annotation.Propagation

class CargaPedidoService implements ExecutableProcessing {

    @Override
    def execute(Date date) {
        def pedidosList = PedidoCarga.withCriteria {
                                            projections {
                                                property "id"
                                            }
                                            eq("status", StatusPedidoCarga.NOVO)
                            }

        if (pedidosList) {
            log.info "${pedidosList.size()} pedido(s) de Carga para processar"
            pedidosList.each { pid ->
                processPedido(pid)
            }
        } else {
            log.warn "Não há Pedidos finalizados para processar!"
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    def processPedido(long id) {
        PedidoCarga pedidoCarga = PedidoCarga.get(id)
        log.info "Processando pedido $pedidoCarga.id ..."
        pedidoCarga.itens.each { ItemPedido i ->
            Transacao tr = new Transacao()
            tr.with {
                tipo = TipoTransacao.CARGA_SALDO
                status = StatusTransacao.AGENDAR
                origem = OrigemTransacao.PORTAL
                dataHora = new Date().clearTime()
                valor = i.valor
            }
            Portador portador
            if (pedidoCarga.unidade.rh.vinculoCartao == TipoVinculoCartao.FUNCIONARIO) {
                tr.participante = i.funcionario
                portador = i.funcionario.portador
            }
            else if (pedidoCarga.unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA) {
                tr.maquina = i.maquina
                portador = i.maquina.portador
            }

            // Associa à transação o cartão ativo do portador
            tr.cartao = portador.cartaoAtivo
            tr.numeroCartao = tr.cartao.numero

            tr.save(flush: true)

            i.transacao = tr
            i.save(flush: true)

            log.info "\tTR CRG #$tr.id criada"

/*
            def oldSaldo = portador.saldoTotal
            portador.saldoTotal += tr.valor
            portador.save(flush: true)
            log.info "\tPRT #$portador.id - (SA: $oldSaldo NS: $portador.saldoTotal)"
*/
        }
        pedidoCarga.status = StatusPedidoCarga.AGENDADO
        pedidoCarga.save(flush: true)

    }

}
