package com.sysdata.gestaofrota.proc.cargaPedido

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import grails.gorm.transactions.Transactional
import org.springframework.transaction.annotation.Propagation

class CargaPedidoService implements ExecutableProcessing {

    @Override
    def execute(Date date) {
        def pedidosList = PedidoCargaInstancia.withCriteria {
                                            projections {
                                                property "id"
                                            }
                                            eq("status", StatusPedidoCarga.NOVO)
                                            le("dataCarga", date)
                            }

        if (pedidosList) {
            log.info "${pedidosList.size()} pedido(s) de Carga para processar"
            pedidosList.each { pid ->
                processPedido(pid, date)
            }
        } else {
            log.warn "Não há Pedidos finalizados para processar!"
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    def processPedido(long id, Date dataRef) {

        PedidoCargaInstancia pedidoCarga = PedidoCargaInstancia.get(id)

        log.info "Processando pedido $pedidoCarga.id ..."

        pedidoCarga.itens.each { ItemPedido item ->

            // Cria apenas transação de carga para portador (funcionário/máquina)

            if (item.instanceOf(ItemPedidoParticipante) || item.instanceOf(ItemPedidoMaquina)) {

                Transacao tr = new Transacao()
                tr.with {
                    tipo = TipoTransacao.CARGA_SALDO
                    status = StatusTransacao.AGENDAR
                    origem = OrigemTransacao.PORTAL
                    dataHora = dataRef
                    valor = item.valor
                }

                Portador portador = item.portador

                // Associa à transação o cartão ativo do portador

                if (item.instanceOf(ItemPedidoParticipante))
                    tr.participante = (item as ItemPedidoParticipante).funcionario
                if (item.instanceOf(ItemPedidoMaquina))
                    tr.maquina = (item as ItemPedidoMaquina).maquina

                tr.cartao = portador.cartaoAtivo
                tr.numeroCartao = tr.cartao.numero

                tr.save(flush: true)

                log.info "\tTR CRG #$tr.id criada"

                LancamentoPortador lctoCarga = new LancamentoPortador()
                lctoCarga.with {
                    tipo = TipoLancamento.CARGA
                    status = StatusLancamento.A_FATURAR
                    valor = tr.valor
                    dataEfetivacao = tr.dataHora
                    conta = portador.conta
                }
                tr.addToLancamentos(lctoCarga)
                tr.save(flush: true)

                item.lancamento = lctoCarga
                item.save(flush: true)

                log.info "\tTR CRG #$tr.id AG"

            // Cria apenas lançamento de taxas
            } else if (item.instanceOf(ItemPedidoTaxa)) {

                TipoLancamento tipoLancamento = item.valor > 0  ? TipoLancamento.TAXA_ADM : TipoLancamento.TAXA_DESCONTO

                LancamentoConvenio lctoTaxa = new LancamentoConvenio()
                lctoTaxa.with {
                    tipo = tipoLancamento
                    status = StatusLancamento.A_FATURAR
                    valor = item.valor
                    dataEfetivacao = dataRef
                    conta = item.pedido.unidade.rh.conta
                }
                lctoTaxa.save(flush: true)

                item.lancamento = lctoTaxa
                item.save(flush: true)

                log.info "\t LC TX #$lctoTaxa.id criado"
            }
        }
        pedidoCarga.status = StatusPedidoCarga.AGENDADO
        pedidoCarga.save(flush: true)

    }

}
