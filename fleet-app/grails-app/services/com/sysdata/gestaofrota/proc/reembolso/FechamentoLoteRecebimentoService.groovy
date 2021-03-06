package com.sysdata.gestaofrota.proc.reembolso

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.agendamento.CalculoDiasUteis
import grails.gorm.transactions.Transactional

@Transactional
class FechamentoLoteRecebimentoService implements ExecutableProcessing, CalculoDiasUteis {

    @Override
    def execute(Date date) {
        log.info "Iniciando Fechamento de Lotes de Recebimentos..."
        if (isDataUtil(date)) {
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

                    def currRhId = 0
                    RecebimentoLote recebimentoLote
                    recIds.each { rid ->
                        RecebimentoConvenio receb = RecebimentoConvenio.get(rid)
                        if (currRhId != receb.rh.id) {
                            recebimentoLote = new RecebimentoLote()
                            Rh rh = receb.rh
                            recebimentoLote.with {
                                dataPrevista = date
                                convenio = rh
                                domicilioBancario = rh.dadoBancario
                                valor = receb.valor
                                valorBruto = receb.valorBruto
                                valorTaxaAdm = receb.valorTaxaAdm
                                recebimentos = [ receb ] as Set
                            }
                            loteReceb.addToRecebimentos(recebimentoLote)
                            loteReceb.save(flush: true)
                            log.info "\tReceb Lote ${recebimentoLote} criado"

                            currRhId = receb.rh.id
                        } else {
                            recebimentoLote.valor += receb.valor
                            recebimentoLote.valorBruto += receb.valorBruto
                            recebimentoLote.valorTaxaAdm += receb.valorTaxaAdm
                            recebimentoLote.save(flush: true)
                        }
                    }
                    loteReceb.status = StatusLotePagamento.FECHADO
                    loteReceb.statusEmissao = StatusEmissao.GERAR_ARQUIVO
                    loteReceb.save(flush: true)
                    log.info "Lote Recebimento #${loteReceb.id} fechado"
                }
            } else
                log.warn "N??o h?? lotes de recebimento para hoje"

        } else
            log.warn "N??o h?? Fechamento de Lotes de Recebimentos em dias n??o ??teis!"

        log.info "Fechamento de Lotes de Recebimentos finalizado"
    }
}
