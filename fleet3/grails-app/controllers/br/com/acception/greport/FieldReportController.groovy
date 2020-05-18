package br.com.acception.greport

import org.springframework.dao.DataIntegrityViolationException

class FieldReportController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		
		def fieldList=FieldReport.withCriteria(max:params.max,offset:params.offset){
							and{
								order "report.id"
								order "order"
							}
						}
		
		
        [fieldReportInstanceList: fieldList, fieldReportInstanceTotal: FieldReport.count()]
    }

    def create() {
        [fieldReportInstance: new FieldReport(params)]
    }

    def save() {
        def fieldReportInstance = new FieldReport(params)
        if (!fieldReportInstance.save(flush: true)) {
            render(view: "create", model: [fieldReportInstance: fieldReportInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'fieldReport.label', default: 'FieldReport'), fieldReportInstance.id])
        redirect(action: "show", id: fieldReportInstance.id)
    }

    def show() {
        def fieldReportInstance = FieldReport.get(params.id)
        if (!fieldReportInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fieldReport.label', default: 'FieldReport'), params.id])
            redirect(action: "list")
            return
        }

        [fieldReportInstance: fieldReportInstance]
    }

    def edit() {
        def fieldReportInstance = FieldReport.get(params.id)
        if (!fieldReportInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'fieldReport.label', default: 'FieldReport'), params.id])
            redirect(action: "list")
            return
        }

        [fieldReportInstance: fieldReportInstance]
    }

    def update() {
        def fieldReportInstance = FieldReport.get(params.id)
        if (!fieldReportInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'fieldReport.label', default: 'FieldReport'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (fieldReportInstance.version > version) {
                fieldReportInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'fieldReport.label', default: 'FieldReport')] as Object[],
                          "Another user has updated this FieldReport while you were editing")
                render(view: "edit", model: [fieldReportInstance: fieldReportInstance])
                return
            }
        }

        fieldReportInstance.properties = params

        if (!fieldReportInstance.save(flush: true)) {
            render(view: "edit", model: [fieldReportInstance: fieldReportInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'fieldReport.label', default: 'FieldReport'), fieldReportInstance.id])
        redirect(action: "show", id: fieldReportInstance.id)
    }

    def delete() {
        def fieldReportInstance = FieldReport.get(params.id)
        if (!fieldReportInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fieldReport.label', default: 'FieldReport'), params.id])
            redirect(action: "list")
            return
        }

        try {
            fieldReportInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'fieldReport.label', default: 'FieldReport'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'fieldReport.label', default: 'FieldReport'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
