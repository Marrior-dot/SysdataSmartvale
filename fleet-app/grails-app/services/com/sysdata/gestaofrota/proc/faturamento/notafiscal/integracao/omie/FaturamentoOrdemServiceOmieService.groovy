package com.sysdata.gestaofrota.proc.faturamento.notafiscal.integracao.omie

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.commons.IntegrationMessengerService
import com.sysdata.gestaofrota.TipoMensagem
import com.sysdata.gestaofrota.http.ResponseData
import com.sysdata.gestaofrota.integracao.omie.OmieOrdemServico
import com.sysdata.gestaofrota.integracao.omie.StatusOrdemServico
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class FaturamentoOrdemServiceOmieService implements ExecutableProcessing {

    GrailsApplication grailsApplication
    IntegrationMessengerService integrationMessengerService

    private void consultarOrdemServico(OmieOrdemServico ordemServico) {

        def omieConfig = grailsApplication.config.projeto.faturamento.portador.notaFiscal.omie

        def filtro = [:]
        filtro.with {
            call = "ConsultarOS"
            app_key = omieConfig.chavesAcesso.appKey
            app_secret = omieConfig.chavesAcesso.appSecret
        }
        filtro.param = [
            [
                cCodIntOS: ordemServico.fatura.id,
                nCodOS: ordemServico.codigoOs,
                cNumOS: ordemServico.numeroOs
            ]
        ]

        ResponseData responseData = integrationMessengerService.postAsJSON(omieConfig.ordemServico.endpoint, TipoMensagem.OMIE_CONSULTAR_OS, filtro)

        if (responseData.statusCode == 200) {

        }

    }



    @Override
    def execute(Date date) {

        OmieOrdemServico ordemServicoList = OmieOrdemServico.findAllByStatusInterno(StatusOrdemServico.CRIADA)

        ordemServicoList.each { os ->

            consultarOrdemServico(os)

        }
    }
}
