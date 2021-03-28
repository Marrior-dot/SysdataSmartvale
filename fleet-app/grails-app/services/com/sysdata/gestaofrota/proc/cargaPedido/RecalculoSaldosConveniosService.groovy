package com.sysdata.gestaofrota.proc.cargaPedido

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.PedidoCarga
import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.StatusPedidoCarga
import grails.gorm.transactions.Transactional

@Transactional
class RecalculoSaldosConveniosService implements ExecutableProcessing {

    private void atualizarSaldoConvenio(Rh convenio, totalGeralPedidos) {
        def novoSaldoConvenio = 0
        def limiteConvenio = convenio.limiteTotal
        if (limiteConvenio - totalGeralPedidos < 0)
            log.warn "\tEMP #${convenio.id} - recalc < 0 = (${limiteConvenio - totalGeralPedidos})"
        novoSaldoConvenio = limiteConvenio - totalGeralPedidos
        log.info "\tEMP #${convenio.id} - LIM:${limiteConvenio} T.P:${totalGeralPedidos} (S.A:${convenio.saldoDisponivel} N.S:${novoSaldoConvenio} DIF:${novoSaldoConvenio - limiteConvenio})"
        convenio.saldoDisponivel = novoSaldoConvenio
        convenio.save(flush: true)
    }

    @Override
    def execute(Date date) {
        log.info "Recuperando Pedidos de Carga p/ Recalcular Saldos dos Clientes..."
        def pedidoCargaList = PedidoCarga.withCriteria {
                                    projections {
                                        property("id")
                                    }
                                    createAlias("unidade", "un")
                                    ne("status", StatusPedidoCarga.CANCELADO)
                                    order("un.rh")
                                }
        if (pedidoCargaList) {
            Rh convenio
            def totalGeralPedidos = 0
            pedidoCargaList.eachWithIndex { pid, idx ->
                PedidoCarga pedidoCarga = PedidoCarga.get(pid)
                if (convenio && convenio != pedidoCarga.unidade.rh) {
                    atualizarSaldoConvenio(convenio, totalGeralPedidos)
                    // Aponta p/ convênio corrente na iteração
                    convenio = pedidoCarga.unidade.rh
                    totalGeralPedidos = 0
                } else {
                    if (!convenio)
                        convenio = pedidoCarga.unidade.rh
                    totalGeralPedidos += pedidoCarga.total
                }
                if ((idx + 1) % 50 == 0)
                    clearSession()
            }
            atualizarSaldoConvenio(convenio, totalGeralPedidos)

        } else
            log.warn "Não há pedidos de carga p/ processar"
    }
}
