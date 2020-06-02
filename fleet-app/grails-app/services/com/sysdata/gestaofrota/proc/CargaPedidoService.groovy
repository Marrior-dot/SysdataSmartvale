package com.sysdata.gestaofrota.proc

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
                                            eq("status", StatusPedidoCarga.LIBERADO)
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
                //cartao = i.funcionario.portador.cartaoAtual
                participante = i.funcionario
            }
            tr.save(flush: true)
            log.info "\tTR CRG #$tr.id criada"
            Portador portador = i.funcionario.portador
            def oldSaldo = portador.saldoTotal
            portador.saldoTotal += tr.valor
            portador.save(flush: true)
            log.info "\tPRT #$portador.id - (SA: $oldSaldo NS: $portador.saldoTotal)"
        }
        pedidoCarga.status = StatusPedidoCarga.FINALIZADO
        pedidoCarga.save(flush: true)

    }

}
