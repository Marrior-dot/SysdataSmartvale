package com.sysdata.gestaofrota

import grails.converters.JSON
import org.springframework.dao.DataIntegrityViolationException

class CartaoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
    }

    def create() {
        [cartaoInstance: new Cartao(params)]
    }

    def save() {
        def cartaoInstance = new Cartao(params)
        if (!cartaoInstance.save(flush: true)) {
            render(view: "create", model: [cartaoInstance: cartaoInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'cartao.label', default: 'Cartao'), cartaoInstance.id])
        redirect(action: "show", id: cartaoInstance.id)
    }

    def show() {
        def cartaoInstance = Cartao.get(params.id)
        if (!cartaoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'cartao.label', default: 'Cartao'), params.id])
            redirect(action: "list")
            return
        }

        [cartaoInstance: cartaoInstance]
    }

    def edit() {
        def cartaoInstance = Cartao.get(params.id)
        if (!cartaoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'cartao.label', default: 'Cartao'), params.id])
            redirect(action: "list")
            return
        }

        [cartaoInstance: cartaoInstance]
    }

    def update() {
        def cartaoInstance = Cartao.get(params.id)
        if (!cartaoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'cartao.label', default: 'Cartao'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (cartaoInstance.version > version) {
                cartaoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'cartao.label', default: 'Cartao')] as Object[],
                        "Another user has updated this Cartao while you were editing")
                render(view: "edit", model: [cartaoInstance: cartaoInstance])
                return
            }
        }

        cartaoInstance.properties = params

        if (!cartaoInstance.save(flush: true)) {
            render(view: "edit", model: [cartaoInstance: cartaoInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'cartao.label', default: 'Cartao'), cartaoInstance.id])
        redirect(action: "show", id: cartaoInstance.id)
    }

    def delete() {
        def cartaoInstance = Cartao.get(params.id)
        if (!cartaoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'cartao.label', default: 'Cartao'), params.id])
            redirect(action: "list")
            return
        }

        try {
            cartaoInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'cartao.label', default: 'Cartao'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'cartao.label', default: 'Cartao'), params.id])
            redirect(action: "show", id: params.id)
        }
    }

    def listAllJSON() {

        def criteria = {
            ne("status", StatusCartao.CANCELADO)
        }

        def cartaoList = Cartao.createCriteria().list(criteria)
        def cartaoCount = Cartao.createCriteria().count(criteria)

        def fields = cartaoList.collect { c ->
            [
                numero   : """<a href='${createLink(action: 'show', id: c.id)}'>${c.numeroMascarado}</a>""",
                portador : c.portador.nomeEmbossing,
                validade : c.validade.format('dd/MM/yyyy'),
                status   : c.status.nome
            ]
        }
        def data = [totalRecords: cartaoCount, results: fields]
        render data as JSON
    }

}
