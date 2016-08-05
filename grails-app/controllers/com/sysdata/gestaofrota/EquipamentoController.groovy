package com.sysdata.gestaofrota

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.springframework.http.HttpStatus

@Secured(['IS_AUTHENTICATED_FULLY'])
class EquipamentoController extends BaseOwnerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def selectRhUnidade = {
        render(view: '/selectRhUnidade', model: [controller: "equipamento", action: Util.ACTION_NEW])
    }

    def list = {

    }

    def newList = {}

    def create = {
        if (params.unidade_id) {
            def unidadeInstance = Unidade.get(params.long('unidade_id'))
            render(view: "form", model: [unidadeInstance: unidadeInstance, action: Util.ACTION_NEW])
        } else {
            flash.message = "Unidade não selecionada!"
            redirect(action: 'list')
        }
    }

    def save = {
        Equipamento.withTransaction {
            if (params.unidId) {
                def equipamentoInstance = new Equipamento(params)
                def unidadeInstance = Unidade.get(params.unidId)
                equipamentoInstance.unidade = unidadeInstance
                if (equipamentoInstance.save(flush: true)) {
                    flash.message = "${message(code: 'default.created.message', args: [message(code: 'equipamento.label', default: ''), equipamentoInstance.id])}"
                    redirect(action: "show", id: equipamentoInstance.id)
                } else {
                    render(view: "form", model: [equipamentoInstance: equipamentoInstance, unidadeInstance: equipamentoInstance.unidade, action: Util.ACTION_NEW])
                }
            }
        }
    }

    def show = {
        def equipamentoInstance = Equipamento.get(params.long('id'))
        if (!equipamentoInstance) {
            flash.errors = "${message(code: 'default.not.found.message', args: [message(code: 'equipamento.label', default: ''), params.id])}"
            redirect(action: "list")
            return;
        }

        render(view: 'form', model: [equipamentoInstance: equipamentoInstance, unidadeInstance: equipamentoInstance.unidade, action: Util.ACTION_VIEW])
    }

    def edit = {
        def equipamentoInstance = Equipamento.get(params.id)
        if (!equipamentoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'veiculo.label', default: ''), params.id])}"
            redirect(action: "list")
        } else {
            render(view: 'form', model: [equipamentoInstance: equipamentoInstance, unidadeInstance: equipamentoInstance.unidade, action: 'editando'])
        }
    }

    def update = {
        def equipamentoInstance = Equipamento.get(params.long('id'))
        if (equipamentoInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (equipamentoInstance.version > version) {

                    equipamentoInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'equipamento.label', default: '')] as Object[], "Another user has updated this Veiculo while you were editing")
                    render(view: 'form', model: [equipamentoInstance: equipamentoInstance, unidadeInstance: equipamentoInstance.unidade, action: 'editando'])
                    return
                }
            }
            equipamentoInstance.properties = params
            if (!equipamentoInstance.hasErrors() && equipamentoInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'equipamento.label', default: 'Equipamento'), equipamentoInstance.id])}"
                redirect(action: "show", id: equipamentoInstance.id)
            } else {
                render(view: 'form', model: [equipamentoInstance: equipamentoInstance, unidadeInstance: equipamentoInstance.unidade, action: 'editando'])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'equipamento.label', default: ''), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def equipamentoInstance = Equipamento.get(params.id)
        if (equipamentoInstance) {
            try {
                equipamentoInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'equipamento.label', default: 'Equipamento'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'equipamento.label', default: 'Equipamento'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'equipamento.label', default: 'Equipamento'), params.id])}"
            redirect(action: "list")
        }
    }

    def listAllJSON = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
//        def offset = params.start ?: 10
        def opcao
        def filtro
        def unidId = params.unidade_id ? (params.unidade_id != 'null' ? params.unidade_id.toLong() : null) : null

        def equipInstanceList

        withSecurity { ownerList ->
            equipInstanceList = Equipamento
                    .createCriteria()
            //.list(max:params.max,offset:offset){
                    .list() {
                if (ownerList.size > 0)
                    unidade { rh { 'in'('id', ownerList) } }

                if (unidId)
                    unidade { eq('id', unidId) }
                if (params.opcao && params.filtro) {
                    opcao = params.opcao.toInteger()
                    filtro = params.filtro
                    //Código
                    if (opcao == 1)
                        like('codigo', filtro + '%')
                    //Descrição
                    else if (opcao == 2)
                        like('descricao', filtro + '%')
                }
            }
        }

        def equipInstanceTotal

        withSecurity { ownerList ->
            equipInstanceTotal = Equipamento.createCriteria().list() {

                if (ownerList.size > 0)
                    unidade { rh { 'in'('id', ownerList) } }

                if (unidId)
                    unidade { eq('id', unidId) }
                if (params.opcao && params.filtro) {
                    //Código
                    if (opcao == 1)
                        like('codigo', filtro + '%')
                    //Marca
                    else if (opcao == 2)
                        like('descricao', filtro + '%')
                }
                projections { rowCount() }
            }
        }


        def fields = equipInstanceList.collect { e ->
            [
                    id       : e.id,
                    codigo   : "<a href=${createLink(action: 'show')}/${e.id}>${e.codigo}</a>",
                    descricao: e.descricao,
                    tipo     : e.tipo?.nome
            ]
        }

        def data = [recordsTotal: equipInstanceTotal, results: fields]
        render data as JSON
    }

}
