package com.sysdata.gestaofrota.proc.notafiscal.barueri

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.StatusEmissao
import grails.gorm.transactions.Transactional

@Transactional
class GeracaoArquivoRPSBarueriService implements ExecutableProcessing {

    @Override
    def execute(Date date) {

        def faturaEnviarList = Fatura.findAllByStatusEmissao(StatusEmissao.ENVIAR, [sort: 'data'])

        faturaEnviarList.each { fat ->



            fat.statusEmissao = StatusEmissao.ENVIADO
            fat.save()
        }

    }
}
