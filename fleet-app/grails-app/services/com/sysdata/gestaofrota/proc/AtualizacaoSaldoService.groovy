package com.sysdata.gestaofrota.proc

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
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
                log.info "Processando Empresa #${rh.id} $rh.nomeEmbossing ..."
                def portadorIds = Portador.withCriteria {
                                                projections {
                                                    property "id"
                                                }
                                                unidade {
                                                    eq("rh", rh)
                                                }
                                                eq("status", Status.ATIVO)
                                                order("id")
                                            }
                portadorIds.eachWithIndex { pid, i ->
                    Portador portador = Portador.get(pid)
                    def limite = portador.limiteTotal
                    Conta conta = portador.conta
                    def totalAFaturar = LancamentoPortador.withCriteria(uniqueResult: true) {
                                            projections {
                                                sum("valor")
                                            }
                                            eq("conta", conta)
                                            eq("status", StatusLancamento.A_FATURAR)
                                        } ?: 0
                    def totalTransacoes = Transacao.withCriteria(uniqueResult: true) {
                                                projections {
                                                    sum("valor")
                                                }
                                                'in'("tipo", [TipoTransacao.COMBUSTIVEL, TipoTransacao.SERVICOS])
                                                'in'("statusControle", [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA])
                                                eq("status", StatusTransacao.AGENDAR)
                                                cartao {
                                                    eq("portador", portador)
                                                }
                                            } ?: 0
                    def saldoNovo = limite - (totalTransacoes + totalAFaturar)
                    def saldoAtual = portador.saldoTotal
                    if (saldoNovo != saldoAtual) {
                        portador.saldoTotal = saldoNovo > 0 ? saldoNovo : 0
                        portador.save(flush: true)
                        log.info "PRT #${portador.id} -> SA: ${saldoAtual} NS: ${portador.saldoTotal} (dif: $portador.saldoTotal - saldoAtual})"
                    }
                    if ((i + 1) % 50 == 0)
                        clearSession()
                }
            }
        } else
            log.warn "Não há Empresas modelo Pós-Pago"

    }
}
