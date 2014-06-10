package com.sysdata.gestaofrota

import org.springframework.dao.DataIntegrityViolationException

class TipoEquipamentoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [tipoEquipamentoInstanceList: TipoEquipamento.list(params), tipoEquipamentoInstanceTotal: TipoEquipamento.count()]
    }

    def create() {
        [tipoEquipamentoInstance: new TipoEquipamento(params)]
    }

    def save() {
        def tipoEquipamentoInstance = new TipoEquipamento(params)
        if (!tipoEquipamentoInstance.save(flush: true)) {
            render(view: "create", model: [tipoEquipamentoInstance: tipoEquipamentoInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'tipoEquipamento.label', default: 'TipoEquipamento'), tipoEquipamentoInstance.id])
        redirect(action: "show", id: tipoEquipamentoInstance.id)
    }

    def show() {
        def tipoEquipamentoInstance = TipoEquipamento.get(params.id)
        if (!tipoEquipamentoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'tipoEquipamento.label', default: 'TipoEquipamento'), params.id])
            redirect(action: "list")
            return
        }

        [tipoEquipamentoInstance: tipoEquipamentoInstance]
    }

    def edit() {
        def tipoEquipamentoInstance = TipoEquipamento.get(params.id)
        if (!tipoEquipamentoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tipoEquipamento.label', default: 'TipoEquipamento'), params.id])
            redirect(action: "list")
            return
        }

        [tipoEquipamentoInstance: tipoEquipamentoInstance]
    }

    def update() {
        def tipoEquipamentoInstance = TipoEquipamento.get(params.id)
        if (!tipoEquipamentoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tipoEquipamento.label', default: 'TipoEquipamento'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (tipoEquipamentoInstance.version > version) {
                tipoEquipamentoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'tipoEquipamento.label', default: 'TipoEquipamento')] as Object[],
                          "Another user has updated this TipoEquipamento while you were editing")
                render(view: "edit", model: [tipoEquipamentoInstance: tipoEquipamentoInstance])
                return
            }
        }

        tipoEquipamentoInstance.properties = params

        if (!tipoEquipamentoInstance.save(flush: true)) {
            render(view: "edit", model: [tipoEquipamentoInstance: tipoEquipamentoInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'tipoEquipamento.label', default: 'TipoEquipamento'), tipoEquipamentoInstance.id])
        redirect(action: "show", id: tipoEquipamentoInstance.id)
    }

    def delete() {
        def tipoEquipamentoInstance = TipoEquipamento.get(params.id)
        if (!tipoEquipamentoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'tipoEquipamento.label', default: 'TipoEquipamento'), params.id])
            redirect(action: "list")
            return
        }

        try {
            tipoEquipamentoInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'tipoEquipamento.label', default: 'TipoEquipamento'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tipoEquipamento.label', default: 'TipoEquipamento'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
