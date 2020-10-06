package com.sysdata.gestaofrota.proc.faturamento

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.Corte
import com.sysdata.gestaofrota.StatusCorte
import org.springframework.transaction.annotation.Transactional

@Transactional
class FaturamentoService implements ExecutableProcessing {

    CorteService corteService

    @Override
    def execute(Date date) {
        log.info "Recuperando cortes abertos e liberados para Faturamento..."

        def cortes = Corte.withCriteria {
                            eq("liberado", true)
                            eq("status", StatusCorte.ABERTO)
                            le("dataPrevista", date)
                        }
        if (cortes.isEmpty())
            log.info "Não há cortes a faturar"
        else {
            cortes.each { cr ->
                corteService.faturar(cr, date)
            }
        }

    }
}
