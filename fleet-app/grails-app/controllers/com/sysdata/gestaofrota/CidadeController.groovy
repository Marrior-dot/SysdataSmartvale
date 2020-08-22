package com.sysdata.gestaofrota

import grails.converters.JSON

class CidadeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        def criteria = {
            if (params.estado) {
                estado {
                    eq("id", params.estado as long)
                }
            }
        }
        params.sort = "estado"
        def cidadeList = Cidade.createCriteria().list(params, criteria)
        def cidadeCount = Cidade.createCriteria().count(criteria)

        [cidadeInstanceList: cidadeList, cidadeInstanceTotal: cidadeCount, params: params]
    }

    def create() {
        def cidadeInstance = new Cidade()
        cidadeInstance.properties = params
        return [cidadeInstance: cidadeInstance]
    }

    def save() {
        def cidadeInstance = new Cidade(params)
        if (cidadeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'cidade.label', default: 'Cidade'), cidadeInstance.id])}"
            redirect(action: "show", id: cidadeInstance.id)
        }
        else {
            render(view: "create", model: [cidadeInstance: cidadeInstance])
        }
    }

    def show() {
        def cidadeInstance = Cidade.get(params.id)
        if (!cidadeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'cidade.label', default: 'Cidade'), params.id])}"
            redirect(action: "list")
        }
        else {
            [cidadeInstance: cidadeInstance]
        }
    }

    def edit() {
        def cidadeInstance = Cidade.get(params.id)
        if (!cidadeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'cidade.label', default: 'Cidade'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [cidadeInstance: cidadeInstance]
        }
    }

    def update() {
        def cidadeInstance = Cidade.get(params.id)
        if (cidadeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (cidadeInstance.version > version) {
                    
                    cidadeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'cidade.label', default: 'Cidade')] as Object[], "Another user has updated this Cidade while you were editing")
                    render(view: "edit", model: [cidadeInstance: cidadeInstance])
                    return
                }
            }
            cidadeInstance.properties = params
            if (!cidadeInstance.hasErrors() && cidadeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'cidade.label', default: 'Cidade'), cidadeInstance.id])}"
                redirect(action: "show", id: cidadeInstance.id)
            }
            else {
                render(view: "edit", model: [cidadeInstance: cidadeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'cidade.label', default: 'Cidade'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete() {
        def cidadeInstance = Cidade.get(params.id)
        if (cidadeInstance) {
            try {
                cidadeInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'cidade.label', default: 'Cidade'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'cidade.label', default: 'Cidade'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'cidade.label', default: 'Cidade'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def listByEstadoJSON={
		if(params.estId){
			def cidadesList=Cidade.withCriteria(){
				estado{eq("id",params.estId.toLong())}
				if(params.query)
					like('nome',params.query+"%")
			}
			def jsonList=cidadesList.collect{[id:it.id, name:it.nome]}
			def result=[result:jsonList]
			render result as JSON
		}
	}
}
