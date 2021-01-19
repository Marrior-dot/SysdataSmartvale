package com.sysdata.gestaofrota.proc.reembolso

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import grails.gorm.transactions.Transactional

@Transactional
class FechamentoLoteRecebimentoService implements ExecutableProcessing {

    @Override
    def execute(Date date) {

        List<LoteRecebimento> lotesAbertos = LoteRecebimento.findAllByStatus(StatusLotePagamento.ABERTO)

        if (lotesAbertos) {

            lotesAbertos.each { loteReceb ->

                log.info "Lote Receb #${loteReceb.id}:"
                def recIds = LoteRecebimento.withCriteria {
                                createAlias("cortes", "c")
                                createAlias("c.recebimentos", "r")
                                projections {
                                    property("r.id")
                                }
                                eq("id", loteReceb.id)
                                order("r.rh")
                            }


                recIds.each { rid ->

                    RecebimentoConvenio receb = RecebimentoConvenio.get(rid)

                    RecebimentoLote recebimentoLote = new RecebimentoLote()
                    Rh rh = receb.rh
                    recebimentoLote.with {
                        dataPrevista = date
                        convenio = rh
                        domicilioBancario = rh.dadoBancario
                        valor = receb.valor
                        recebimentos = [ receb ] as Set
                    }
                    loteReceb.addToRecebimentos(recebimentoLote)
                    loteReceb.save(flush: true)
                    log.info "\tReceb Lote ${recebimentoLote} criado"
                }

                loteReceb.status = StatusLotePagamento.FECHADO
                loteReceb.save(flush: true)
                log.info "Lote Recebimento #${loteReceb.id} fechado"
            }

        } else
            log.warn "Não há lotes de recebimento para hoje"

    }
}
