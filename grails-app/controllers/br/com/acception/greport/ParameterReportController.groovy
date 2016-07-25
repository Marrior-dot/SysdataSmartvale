package br.com.acception.greport

import org.springframework.dao.DataIntegrityViolationException

class ParameterReportController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [parameterReportInstanceList: ParameterReport.list(params), parameterReportInstanceTotal: ParameterReport.count()]
    }

    def create() {
        [parameterReportInstance: new ParameterReport(params)]
    }

    def save() {
        def parameterReportInstance = new ParameterReport(params)
        if (!parameterReportInstance.save(flush: true)) {
            render(view: "create", model: [parameterReportInstance: parameterReportInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'parameterReport.label', default: 'ParameterReport'), parameterReportInstance.id])
        redirect(action: "show", id: parameterReportInstance.id)
    }

    def show() {
        def parameterReportInstance = ParameterReport.get(params.id)
        if (!parameterReportInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'parameterReport.label', default: 'ParameterReport'), params.id])
            redirect(action: "list")
            return
        }

        [parameterReportInstance: parameterReportInstance]
    }

    def edit() {
        def parameterReportInstance = ParameterReport.get(params.long('id'))
        if (!parameterReportInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'parameterReport.label', default: 'ParameterReport'), params.id])
            redirect(action: "list")
            return
        }

        [parameterReportInstance: parameterReportInstance]
    }

    def update() {
        def parameterReportInstance = ParameterReport.get(params.id)
        if (!parameterReportInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'parameterReport.label', default: 'ParameterReport'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (parameterReportInstance.version > version) {
                parameterReportInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'parameterReport.label', default: 'ParameterReport')] as Object[],
                          "Another user has updated this ParameterReport while you were editing")
                render(view: "edit", model: [parameterReportInstance: parameterReportInstance])
                return
            }
        }

        parameterReportInstance.properties = params

        if (!parameterReportInstance.save(flush: true)) {
            render(view: "edit", model: [parameterReportInstance: parameterReportInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'parameterReport.label', default: 'ParameterReport'), parameterReportInstance.id])
        redirect(action: "show", id: parameterReportInstance.id)
    }

    def delete() {
        def parameterReportInstance = ParameterReport.get(params.id)
        if (!parameterReportInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'parameterReport.label', default: 'ParameterReport'), params.id])
            redirect(action: "list")
            return
        }

        try {
            parameterReportInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'parameterReport.label', default: 'ParameterReport'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'parameterReport.label', default: 'ParameterReport'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
