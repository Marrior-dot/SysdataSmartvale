package com.sysdata.gestaofrota

import javax.servlet.jsp.tagext.TryCatchFinally;

class ParametroSistemaController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [parametroSistemaInstanceList: ParametroSistema.list(params), parametroSistemaInstanceTotal: ParametroSistema.count()]
    }

    def create = {
        def parametroSistemaInstance = new ParametroSistema()
        parametroSistemaInstance.properties = params
        return [parametroSistemaInstance: parametroSistemaInstance]
    }

    def save = {
        def parametroSistemaInstance = new ParametroSistema(params)
		
		parametroSistemaInstance.setValor(params.valor)
		
        if (parametroSistemaInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'parametroSistema.label', default: 'ParametroSistema'), parametroSistemaInstance.id])}"
            redirect(action: "show", id: parametroSistemaInstance.id)
        }
        else {
            render(view: "create", model: [parametroSistemaInstance: parametroSistemaInstance])
        }
    }

    def show = {
        def parametroSistemaInstance = ParametroSistema.get(params.id)
        if (!parametroSistemaInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'parametroSistema.label', default: 'ParametroSistema'), params.id])}"
            redirect(action: "list")
        }
        else {
            [parametroSistemaInstance: parametroSistemaInstance]
        }
    }

    def edit = {
        def parametroSistemaInstance = ParametroSistema.get(params.id)
        if (!parametroSistemaInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'parametroSistema.label', default: 'ParametroSistema'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [parametroSistemaInstance: parametroSistemaInstance]
        }
    }

    def update = {
        def parametroSistemaInstance = ParametroSistema.get(params.id)
		
		try{
			parametroSistemaInstance.setValor(params.valor)
		}catch(Exception e){
			flash.message=e.message
			render(view: "edit", model: [parametroSistemaInstance: parametroSistemaInstance])
			return 
		}
		
        if (parametroSistemaInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (parametroSistemaInstance.version > version) {
                    
                    parametroSistemaInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'parametroSistema.label', default: 'ParametroSistema')] as Object[], "Another user has updated this ParametroSistema while you were editing")
                    render(view: "edit", model: [parametroSistemaInstance: parametroSistemaInstance])
                    return
                }
            }
            parametroSistemaInstance.properties = params
            if (!parametroSistemaInstance.hasErrors() && parametroSistemaInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'parametroSistema.label', default: 'ParametroSistema'), parametroSistemaInstance.id])}"
                redirect(action: "show", id: parametroSistemaInstance.id)
            }
            else {
                render(view: "edit", model: [parametroSistemaInstance: parametroSistemaInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'parametroSistema.label', default: 'ParametroSistema'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def parametroSistemaInstance = ParametroSistema.get(params.id)
        if (parametroSistemaInstance) {
            try {
                parametroSistemaInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'parametroSistema.label', default: 'ParametroSistema'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'parametroSistema.label', default: 'ParametroSistema'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'parametroSistema.label', default: 'ParametroSistema'), params.id])}"
            redirect(action: "list")
        }
    }
}
