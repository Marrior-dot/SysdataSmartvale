package com.sysdata.gestaofrota.proc.faturamento.notafiscal.integracao.omie

import com.sysdata.gestaofrota.integracao.omie.OmieStatusOrdemServico
import grails.gorm.transactions.Transactional

@Transactional
class OmieStatusOSService {

    OmieStatusOrdemServico getStatus(String codStatus, String descStatus) {

        OmieStatusOrdemServico omieStatusOS = OmieStatusOrdemServico.findByCodigo(codStatus)
        if (!omieStatusOS) {
            omieStatusOS = new OmieStatusOrdemServico()
            omieStatusOS.with {
                codigo = codStatus
                descricao = descStatus
            }
            omieStatusOS.save(flush: true)
        }
        return omieStatusOS
    }
}
