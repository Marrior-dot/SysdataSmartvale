package com.sysdata.gestaofrota

import grails.converters.JSON

class MarcaVeiculoController {

    static allowedMethods = [save: "POST", update: "POST", find: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [marcaVeiculoInstanceList: MarcaVeiculo.list(params), marcaVeiculoInstanceTotal: MarcaVeiculo.count()]
    }

    def create = {
        def marcaVeiculoInstance = new MarcaVeiculo()
        marcaVeiculoInstance.properties = params
        return [marcaVeiculoInstance: marcaVeiculoInstance]
    }

    def save = {
        def marcaVeiculoInstance = new MarcaVeiculo(params)
        if (marcaVeiculoInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'marcaVeiculo.label', default: 'MarcaVeiculo'), marcaVeiculoInstance.id])}"
            redirect(action: "show", id: marcaVeiculoInstance.id)
        } else {
            render(view: "create", model: [marcaVeiculoInstance: marcaVeiculoInstance])
        }
    }

    def show = {
        def marcaVeiculoInstance = MarcaVeiculo.get(params.id)
        if (!marcaVeiculoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'marcaVeiculo.label', default: 'MarcaVeiculo'), params.id])}"
            redirect(action: "list")
        } else {
            [marcaVeiculoInstance: marcaVeiculoInstance]
        }
    }

    def edit = {
        def marcaVeiculoInstance = MarcaVeiculo.get(params.id)
        if (!marcaVeiculoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'marcaVeiculo.label', default: 'MarcaVeiculo'), params.id])}"
            redirect(action: "list")
        } else {
            return [marcaVeiculoInstance: marcaVeiculoInstance]
        }
    }

    def update = {
        def marcaVeiculoInstance = MarcaVeiculo.get(params.id)
        if (marcaVeiculoInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (marcaVeiculoInstance.version > version) {

                    marcaVeiculoInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'marcaVeiculo.label', default: 'MarcaVeiculo')] as Object[], "Another user has updated this MarcaVeiculo while you were editing")
                    render(view: "edit", model: [marcaVeiculoInstance: marcaVeiculoInstance])
                    return
                }
            }
            marcaVeiculoInstance.properties = params
            if (!marcaVeiculoInstance.hasErrors() && marcaVeiculoInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'marcaVeiculo.label', default: 'MarcaVeiculo'), marcaVeiculoInstance.id])}"
                redirect(action: "show", id: marcaVeiculoInstance.id)
            } else {
                render(view: "edit", model: [marcaVeiculoInstance: marcaVeiculoInstance])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'marcaVeiculo.label', default: 'MarcaVeiculo'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def marcaVeiculoInstance = MarcaVeiculo.get(params.id)
        if (marcaVeiculoInstance) {
            try {
                marcaVeiculoInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'marcaVeiculo.label', default: 'MarcaVeiculo'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'marcaVeiculo.label', default: 'MarcaVeiculo'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'marcaVeiculo.label', default: 'MarcaVeiculo'), params.id])}"
            redirect(action: "list")
        }
    }
}
