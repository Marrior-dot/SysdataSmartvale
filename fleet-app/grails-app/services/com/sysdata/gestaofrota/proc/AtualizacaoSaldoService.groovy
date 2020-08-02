package com.sysdata.gestaofrota.proc

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Status
import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.TipoCobranca
import com.sysdata.gestaofrota.TipoTransacao
import com.sysdata.gestaofrota.Transacao
import grails.gorm.transactions.Transactional

@Transactional
class AtualizacaoSaldoService implements ExecutableProcessing {

    @Override
    def execute(Date date) {
        def empresasCredito = Rh.withCriteria {
                                eq("modeloCobranca", TipoCobranca.POS_PAGO)
                                eq("status", Status.ATIVO)
                                order("nome")
                            }

        if (! empresasCredito.isEmpty()) {

            empresasCredito.each { Rh rh ->

                log.info "Processando Empresa $rh ..."

                def portadorIds = Portador.withCriteria {
                                                projections {
                                                    property "id"
                                                }
                                                unidade {
                                                    eq("rh", rh)
                                                }
                                                eq("status", Status.ATIVO)
                                            }

                portadorIds.eachWithIndex { pid, i ->
                    Portador portador = Portador.get(pid)
                    def limite = portador.limiteTotal
                    def totalTransacoes = Transacao.withCriteria {
                                                projections {
                                                    sum("valor")
                                                }
                                                'in'("tipo", [TipoTransacao.COMBUSTIVEL, TipoTransacao.SERVICOS])
                                                'in'("statusControle", [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA])
                                                cartao {
                                                    eq("portador", portador)
                                                }
                                            }
                    def novoSaldo = limite - totalTransacoes
                    if (novoSaldo != portador.saldoTotal) {
                        portador.saldoTotal = novoSaldo
                        portador.save(flush: true)
                    }


                    if ((i + 1) % 50 == 0) {

                    }
                }
            }



        } else
            log.warn "Não há Empresas modelo Pós-Pago"

    }
}
