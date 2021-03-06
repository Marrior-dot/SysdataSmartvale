package com.sysdata.gestaofrota

import org.springframework.dao.DataIntegrityViolationException

class MotivoNegacaoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index(Integer max) {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 100, 100)
        params.sort = "codigo"
        params.order = "asc"
        [motivoNegacaoInstanceList: MotivoNegacao.list(params), motivoNegacaoInstanceTotal: MotivoNegacao.count()]
    }

    def create() {
        [motivoNegacaoInstance: new MotivoNegacao(params)]
    }

    def save() {
        def motivoNegacaoInstance = new MotivoNegacao(params)
        if (!motivoNegacaoInstance.save(flush: true)) {
            render(view: "create", model: [motivoNegacaoInstance: motivoNegacaoInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'motivoNegacao.label', default: 'Motivo Negacao'), motivoNegacaoInstance.id])
        redirect(action: "show", id: motivoNegacaoInstance.id)
    }

    def show() {
        def motivoNegacaoInstance = MotivoNegacao.get(params.id)
        if (!motivoNegacaoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'motivoNegacao.label', default: 'Motiv oNegacao'), params.id])
            redirect(action: "list")
            return
        }

        [motivoNegacaoInstance: motivoNegacaoInstance]
    }

    def edit() {
        def motivoNegacaoInstance = MotivoNegacao.get(params.id)
        if (!motivoNegacaoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'motivoNegacao.label', default: 'Motivo Negacao'), params.id])
            redirect(action: "list")
            return
        }

        [motivoNegacaoInstance: motivoNegacaoInstance]
    }

    def update() {
        def motivoNegacaoInstance = MotivoNegacao.get(params.id)
        if (!motivoNegacaoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'motivoNegacao.label', default: 'Motivo Negacao'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (motivoNegacaoInstance.version > version) {
                motivoNegacaoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'motivoNegacao.label', default: 'MotivoNegacao')] as Object[],
                          "Another user has updated this MotivoNegacao while you were editing")
                render(view: "edit", model: [motivoNegacaoInstance: motivoNegacaoInstance])
                return
            }
        }

        motivoNegacaoInstance.properties = params

        if (!motivoNegacaoInstance.save(flush: true)) {
            render(view: "edit", model: [motivoNegacaoInstance: motivoNegacaoInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'motivoNegacao.label', default: 'Motivo Negacao'), motivoNegacaoInstance.id])
        redirect(action: "show", id: motivoNegacaoInstance.id)
    }

    def delete() {
        def motivoNegacaoInstance = MotivoNegacao.get(params.id)
        if (!motivoNegacaoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'motivoNegacao.label', default: 'Motivo Negacao'), params.id])
            redirect(action: "list")
            return
        }

        try {
            motivoNegacaoInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'motivoNegacao.label', default: 'Motivo Negacao'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'motivoNegacao.label', default: 'Motivo Negacao'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
