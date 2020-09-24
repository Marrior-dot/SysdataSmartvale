package com.sysdata.gestaofrota.proc.agendamento

import com.sysdata.gestaofrota.PostoCombustivel
import com.sysdata.gestaofrota.TipoReembolso
import grails.gorm.transactions.Transactional

@Transactional
class ReembolsoService {

    ReembolsoIntervaloService reembolsoIntervaloService
    ReembolsoSemanalService reembolsoSemanalService

    Date calcularDataReembolso(PostoCombustivel empresa, Date dataReferencia) {

        if (empresa.tipoReembolso == TipoReembolso.INTERVALOS_MULTIPLOS) {
            return reembolsoIntervaloService.calcularDataReembolso(empresa, dataReferencia)

        } else if (empresa.tipoReembolso == TipoReembolso.SEMANAL) {
            return reembolsoSemanalService.calcularDataReembolso(empresa, dataReferencia)

        } else
            throw new RuntimeException("Tipo de Reembolso indefinido!!!")
    }

    Date calcularProximaDataReembolso(PostoCombustivel empresa, Date dataReferencia) {

    }
}
