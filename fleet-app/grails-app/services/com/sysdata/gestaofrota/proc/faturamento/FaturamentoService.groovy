package com.sysdata.gestaofrota.proc.faturamento

import com.sysdata.gestaofrota.Corte
import com.sysdata.gestaofrota.StatusCorte
import com.sysdata.gestaofrota.proc.Processamento
import org.springframework.transaction.annotation.Transactional

class FaturamentoService implements Processamento {

    @Transactional
    @Override
    def executar(Date dataProc) {
        log.info "Recuperando cortes abertos e liberados para Faturamento..."

        def cortes=Corte.withCriteria {
            eq("liberado",true)
            eq("status",StatusCorte.ABERTO)
            le("dataPrevista",dataProc)
        }

        if(cortes.isEmpty()) log.info "Nao ha cortes para faturar"

        cortes.each{cr->
            cr.faturar(dataProc)
        }
    }
}
