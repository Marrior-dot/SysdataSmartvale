package com.sysdata.gestaofrota

class BancoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [bancoInstanceList: Banco.list(params), bancoInstanceTotal: Banco.count()]
    }

    def create = {
        def bancoInstance = new Banco()
        bancoInstance.properties = params
        return [bancoInstance: bancoInstance]
    }

    def save = {
        def bancoInstance = new Banco(params)
        if (bancoInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'banco.label', default: 'Banco'), bancoInstance.id])}"
            redirect(action: "show", id: bancoInstance.id)
        }
        else {
            render(view: "create", model: [bancoInstance: bancoInstance])
        }
    }

    def show = {
        def bancoInstance = Banco.get(params.id)
        if (!bancoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'banco.label', default: 'Banco'), params.id])}"
            redirect(action: "list")
        }
        else {
            [bancoInstance: bancoInstance]
        }
    }

    def edit = {
        def bancoInstance = Banco.get(params.id)
        if (!bancoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'banco.label', default: 'Banco'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [bancoInstance: bancoInstance]
        }
    }

    def update = {
        def bancoInstance = Banco.get(params.id)
        if (bancoInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (bancoInstance.version > version) {
                    
                    bancoInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'banco.label', default: 'Banco')] as Object[], "Another user has updated this Banco while you were editing")
                    render(view: "edit", model: [bancoInstance: bancoInstance])
                    return
                }
            }
            bancoInstance.properties = params
            if (!bancoInstance.hasErrors() && bancoInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'banco.label', default: 'Banco'), bancoInstance.id])}"
                redirect(action: "show", id: bancoInstance.id)
            }
            else {
                render(view: "edit", model: [bancoInstance: bancoInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'banco.label', default: 'Banco'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def bancoInstance = Banco.get(params.id)
        if (bancoInstance) {
            try {
                bancoInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'banco.label', default: 'Banco'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'banco.label', default: 'Banco'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'banco.label', default: 'Banco'), params.id])}"
            redirect(action: "list")
        }
    }
}
