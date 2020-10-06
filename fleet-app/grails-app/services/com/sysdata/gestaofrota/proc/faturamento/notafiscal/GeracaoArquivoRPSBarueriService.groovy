package com.sysdata.gestaofrota.proc.faturamento.notafiscal

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.StatusEmissao
import grails.gorm.transactions.Transactional

@Transactional
class GeracaoArquivoRPSBarueriService implements ExecutableProcessing {

    @Override
    def execute(Date date) {

        def faturaEnviarList = Fatura.findAllByStatusEmissao(StatusEmissao.GERAR_ARQUIVO, [sort: 'data'])

        faturaEnviarList.each { fat ->



            fat.statusEmissao = StatusEmissao.ARQUIVO_GERADO
            fat.save()
        }

    }
}
