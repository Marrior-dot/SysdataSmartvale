package com.sysdata.gestaofrota.proc.faturamento.notafiscal.integracao.omie

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.commons.IntegrationMessengerService
import com.sysdata.gestaofrota.TipoMensagem
import com.sysdata.gestaofrota.http.ResponseData
import com.sysdata.gestaofrota.integracao.omie.OmieOrdemServico
import com.sysdata.gestaofrota.integracao.omie.OmieStatusOrdemServico
import com.sysdata.gestaofrota.integracao.omie.StatusOrdemServico
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class FaturamentoOrdemServiceOmieService implements ExecutableProcessing {

    GrailsApplication grailsApplication
    IntegrationMessengerService integrationMessengerService
    OmieStatusOSService omieStatusOSService

    private boolean validarOrdemServico(OmieOrdemServico ordemServico) {

        def omieConfig = grailsApplication.config.projeto.faturamento.portador.notaFiscal.omie

        def filtro = [:]
        filtro.with {
            call = "ValidarOS"
            app_key = omieConfig.chavesAcesso.appKey
            app_secret = omieConfig.chavesAcesso.appSecret
        }
        filtro.param = [
            [
                cCodIntOS: ordemServico.fatura.id,
                nCodOS: ordemServico.codigoOs,
            ]
        ]

        ResponseData responseData = integrationMessengerService.postAsJSON(omieConfig.ordemServicoFaturamento.endpoint,
                                                                            TipoMensagem.OMIE_VALIDAR_OS,
                                                                            filtro)
        if (responseData.statusCode == 200) {
            OmieStatusOrdemServico omieStatusOS = omieStatusOSService.getStatus(responseData.json.cCodStatus, responseData.json.cDescStatus)
            ordemServico.statusOs = omieStatusOS
            ordemServico.save(flush: true)

            if (ordemServico.statusOs.codigo != "0")
                ordemServico.statusInterno = StatusOrdemServico.INVALIDA

            return (ordemServico.statusOs.codigo == "0")

        } else if (responseData.statusCode == 500) {
            log.debug "Erro: $responseData.json"
            return false
        }

    }


    private void faturarOSemOmie(OmieOrdemServico os) {

        def omieConfig = grailsApplication.config.projeto.faturamento.portador.notaFiscal.omie

        def filtro = [:]
        filtro.with {
            call = "FaturarOS"
            app_key = omieConfig.chavesAcesso.appKey
            app_secret = omieConfig.chavesAcesso.appSecret
        }
        filtro.param = [
                [
                    cCodIntOS: os.fatura.id,
                    nCodOS   : os.codigoOs,
                ]
        ]

        ResponseData responseData = integrationMessengerService.postAsJSON(omieConfig.ordemServicoFaturamento.endpoint,
                                                                            TipoMensagem.OMIE_FATURAR_OS,
                                                                            filtro)

        if (responseData.statusCode == 200) {
            OmieStatusOrdemServico omieStatusOS = omieStatusOSService.getStatus(responseData.json.cCodStatus, responseData.json.cDescStatus)
            os.statusOs = omieStatusOS
            os.statusInterno = StatusOrdemServico.FATURADA
            os.save(flush: true)
        } else if (responseData.statusCode == 500) {
            log.debug "Erro: $responseData.json"
        }
    }


    @Override
    def execute(Date date) {

        log.info "Recuperando OS para faturar em Omie..."
        def ordemServicoList = OmieOrdemServico.findAllByStatusInterno(StatusOrdemServico.CRIADA)

        ordemServicoList.each { os ->

            log.info "Faturando OS ${os.id} ..."
            if (validarOrdemServico(os))
                faturarOSemOmie(os)
            else
                log.error "OS #${os.codigoOs} inválida para faturar em Omie"
        }
    }
}
