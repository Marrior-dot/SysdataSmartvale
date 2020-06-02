package br.com.acception.greport

import com.sysdata.gestaofrota.*
import grails.core.GrailsApplication
import grails.plugin.springsecurity.SpringSecurityUtils
import org.hibernate.QueryException

class ReportViewerController {
    def springSecurityService
    def reportViewerService
    def xlsExportService
    GrailsApplication grailsApplication

    def index() {
        redirect action: 'listReports', params: params
    }


    def listReports() {
        def reportList = Report.all
        [reportList: reportList]
    }

    def openToParameters() {
        def reportInstance = Report.get(params.long('id'))
        if (!reportInstance) {
            flash.message = "Relatório ${params.id} não encontrado na base de configuração"
            redirect(action: 'listReports')
            return;
        }

        render view: "list", model: [reportInstance: reportInstance, rowCount: 0]
    }

    def export() {
        params.max = Math.min(params.max ? params.int('max') : 50, 100)

        def reportInstance = Report.get(params.long('id'))

        Participante participanteInstance = springSecurityService?.getCurrentUser()?.owner
        Estabelecimento estabelecimentoInstance = null

        if (SpringSecurityUtils.ifAllGranted("ROLE_ESTAB") && participanteInstance?.instanceOf(PostoCombustivel)) {
            PostoCombustivel postoCombustivel = PostoCombustivel.get(participanteInstance.id)
            estabelecimentoInstance = Estabelecimento.findByEmpresa(postoCombustivel)
        }
        params << ['codEstab': estabelecimentoInstance?.codigo ?: '']

        def result = [:]
        if (reportInstance) {
            params.reportInstance = reportInstance
            try {
                /* Trata solicitação de geração XLSX */
                response.contentType = grailsApplication.config.grails.mime.types[params.format]
                response.setHeader("Content-disposition", "attachment;filename=${reportInstance.name}.xlsx")
                xlsExportService.export(params, response.outputStream)

            } catch (Exception e) {
                log.error e.message
                flash.errors = e.message
            }
        }

        render view: 'list', model: params + result
    }

    def showProjecaoPagar() {
        def reportList = Report.all
        [reportList: reportList]
    }


    def list() {
        Report reportInstance = Report.get(params.long('id'))
        if (!reportInstance) {
            flash.errors = "Report não encontrado"
            redirect(action: 'list')
            return;
        }

        Participante participanteInstance = springSecurityService?.getCurrentUser()?.owner
        Estabelecimento estabelecimentoInstance = null

        if (SpringSecurityUtils.ifAllGranted("ROLE_ESTAB") && participanteInstance?.instanceOf(PostoCombustivel)) {
            PostoCombustivel postoCombustivel = PostoCombustivel.get(participanteInstance.id)
            estabelecimentoInstance = Estabelecimento.findByEmpresa(postoCombustivel)
        }

        params.codEstab = estabelecimentoInstance?.codigo ?: ''
        params.max = params.max ?: 10
        params.offset = params.offset ?: 0

        def result = [:]

        try {
            result = reportViewerService.list(params)
        } catch (QueryException e) {
            result = [rows: null, rowCount: 0, rowTotal: 0]
            log.error(e)
            flash.errors = e
        } finally {
            //session foi finalizado devido ao catch acima. Recarregar o reportInstance
            params.reportInstance = Report.get(params.long('id'))
        }

        params + result
    }
}
