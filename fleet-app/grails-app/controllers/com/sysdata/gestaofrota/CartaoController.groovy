package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.cartao.ResetSenhaCartaoService
import com.sysdata.gestaofrota.exception.BusinessException
import grails.converters.JSON

class CartaoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]


    ResetSenhaCartaoService resetSenhaCartaoService


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
            if (params.tipo) {
                eq("tipo", TipoCartao.valueOf(params.tipo))
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

    def resetSenha() {
        if (params.id && params.id ==~ /\d+/) {
            Cartao cartao = Cartao.get(params.id.toLong())
            try {
                def ret = resetSenhaCartaoService.resetSenha(cartao)
                if (ret.success)
                    flash.success = "Reset de Senha agendado"
                else
                    flash.error = ret.message
            } catch (e) {
                e.printStackTrace()
                flash.error = "Erro interno! Contatar suporte."
            }
            render view: 'show', model: [cartaoInstance: cartao]
            return
        } else {
            flash.error = "ID Cart??o '${params.id}' inv??lido!"
            redirect action: 'index'
            return
        }
    }

    def findAllCartoesPortador() {
        if (params.prtId && params.prtId ==~ /\d+/) {
            Portador portador = Portador.get(params.prtId as long)
            if (portador) {
                render template: "cartoes", model: [portador: portador]
                return
            } else {
                render status: 404, text: "N??o encontrado Portador com ID #${params.prtId}!"
                return
            }
        } else {
            render status: 404, text: "Portador ID informado na request inv??lido: ${params.prtId}!"
            return
        }
    }

    def findByNumero() {
        Cartao cartao = Cartao.findByNumero(params.cartao)
        if (! cartao)
            render status: 404, text: "Cart??o ${params.cartao} n??o localizado na base!"
        else
            render status: 200, text: cartao.portador.saldoTotal
    }
}
