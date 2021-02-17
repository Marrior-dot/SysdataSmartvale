package com.sysdata.gestaofrota

import grails.converters.JSON
import org.springframework.dao.DataIntegrityViolationException

class CartaoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = params.max ? params.max as int: 10
        params.sort = "dateCreated"
        params.order = "desc"

        def criteria = {

            if (params.numero) {
                eq("numero", params.numero.trim())
            }

            if (params.unidade) {
                portador {
                    unidade {
                        eq("id", params.unidade.toLong())
                    }
                }
            }
            if (params.status) {
                eq("status", StatusCartao.valueOf(params.status))
            }
            ne("status", StatusCartao.CANCELADO)
        }

        def cartaoList = Cartao.createCriteria().list(params, criteria)
        def cartaoCount = Cartao.createCriteria().count(criteria)

        [cartaoList: cartaoList, cartaoCount: cartaoCount, params: params]
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
