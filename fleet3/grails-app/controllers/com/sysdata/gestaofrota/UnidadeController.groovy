package com.sysdata.gestaofrota

import grails.converters.JSON


//@Secured(['IS_AUTHENTICATED_FULLY'])
class UnidadeController {

    def geracaoCartaSenhaService
    def unidadeService

    static allowedMethods = [save: "POST", update: "POST", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        redirect(controller: 'rh', action: 'show', params: [id: params.rhId])
    }

    def find() {
        long rhId = params.long('rh.id')
        String nome = params['nome']

        List<Unidade> unidadeList = Unidade.createCriteria().list(params) {
            if (rhId != null) {
                rh { idEq(rhId) }
            }
            if (nome?.length() > 0) ilike('nome', "%${nome}%")
        }

        withFormat {
            json { render unidadeList as JSON }
        }
    }

    def create() {
        def rhInstance = Rh.get(params.rhId)
        render(view: 'form', model: [action: Util.ACTION_NEW, rhInstance: rhInstance])
    }

    def save() {
        if (params.rhId) {
            def rhId = params.rhId
            def unidadeInstance = new Unidade(params)
            def rhInstance = Rh.get(rhId as Long)
            unidadeInstance.rh = rhInstance
            if (unidadeService.saveNew(unidadeInstance)) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'unidade.label', default: 'Unidade'), unidadeInstance.id])}"
                redirect(action: "show", id: unidadeInstance.id)
            } else {
                render(view: "form", model: [unidadeInstance: unidadeInstance, rhInstance: unidadeInstance.rh, action: Util.ACTION_NEW])
            }
        } else {
            log.error "Unidade não relacionada a um Rh específico!"
            redirect(action: "list")
        }
    }

    def show() {
        def unidadeInstance = Unidade.get(params.id)
        if (!unidadeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'unidade.label', default: 'Unidade'), params.id])}"
            redirect(action: "list", params: [rhId: params.rhId])
        } else {
            render(view: 'form', model: [unidadeInstance: unidadeInstance, rhInstance: unidadeInstance.rh, action: Util.ACTION_VIEW])
        }
    }

    def edit() {
        def unidadeInstance = Unidade.get(params.id)
        if (!unidadeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'unidade.label', default: 'Unidade'), params.id])}"
            redirect(action: "list", params: [rhId: params.rhId])
        } else {
            render(view: "form", model: [unidadeInstance: unidadeInstance, rhInstance: unidadeInstance.rh, action: Util.ACTION_EDIT])
        }
    }

    def update() {
        def unidadeInstance = Unidade.get(params.id)
        if (unidadeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (unidadeInstance.version > version) {

                    unidadeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'unidade.label', default: 'Unidade')] as Object[], "Another user has updated this Unidade while you were editing")
                    render(view: 'form', model: [unidadeInstance: unidadeInstance, rhInstance: unidadeInstance.rh, action: Util.ACTION_EDIT])
                    return
                }
            }
            unidadeInstance.properties = params
            if (!unidadeInstance.hasErrors() && unidadeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'unidade.label', default: 'Unidade'), unidadeInstance.id])}"
                redirect(action: "show", id: unidadeInstance.id)
            } else {
                render(view: 'form', model: [unidadeInstance: unidadeInstance, rhInstance: unidadeInstance.rh, action: Util.ACTION_EDIT])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'unidade.label', default: 'Unidade'), params.id])}"
            redirect(action: "list", params: [rhId: params.rhId])
        }
    }

    def delete() {
        def unidadeInstance = Unidade.get(params.id)
        if (unidadeInstance) {
            def ret = unidadeService.delete(unidadeInstance)
             flash.message = ret.message
             redirect(controller: 'rh', action: 'show', params: [id: unidadeInstance.rh.id])
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'unidade.label', default: 'Unidade')])}"
            redirect(action: "list")
        }
    }

    def autoCompleteJSON() {
        long rhId = params?.long('rhId') ?: 0
        def list = Unidade.withCriteria {
            rh { idEq(rhId) }
            if (params?.query?.toString().length() > 0)
                like("nome", params.query + "%")
        }
        def jsonList = list.collect { [id: it.id, name: it.nome] }
        def jsonResult = [
                result: jsonList
        ]
        render jsonResult as JSON
    }

    def listAllJSON() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def offset = params.offset ?: 0
        def opcao
        def filtro
        def rhId = params.rhId ? params.rhId.toLong() : null

        def unidadeInstanceList = Unidade
                .createCriteria()
                .list(max: params.max, offset: offset) {
            if (rhId)
                rh { eq('id', rhId) }
            if (params.opcao && params.filtro) {
                opcao = params.opcao.toInteger()
                filtro = params.filtro
                //Código
                if (opcao == 1)
                    eq('id', filtro as long)
                //Nome
                else if (opcao == 2)
                    like('nome', filtro + '%')
            }
        }
        def unidadeInstanceTotal = Unidade
                .createCriteria()
                .list() {
            if (rhId)
                rh { eq('id', rhId) }
            if (params.opcao && params.filtro) {
                opcao = params.opcao.toInteger()
                filtro = params.filtro
                //Código
                if (opcao == 1)
                    eq('id', filtro as long)
                //Nome
                else if (opcao == 2)
                    like('nome', filtro + '%')
            }
            projections { rowCount() }
        }

        def fields = unidadeInstanceList.collect { u ->
            [id    : u.id,
             nome  : u.nome,
             status: u.status.nome,
             acoes : """<a class="show" href='${createLink(action: 'show')}/${u.id}'></a> |

									${
                 if (u.status == Status.ATIVO)
                     "<a href='${createLink(action: 'block', id: u.id)}'>Bloquear</a> | <a href='${createLink(action: 'unable', id: u.id)}'>Inativar</a>"
                 else if (u.status == Status.BLOQUEADO)
                     "<a href='${createLink(action: 'enable', id: u.id)}'>Desbloquear</a> | <a href='${createLink(action: 'unable', id: u.id)}'>Inativar</a>"
                 else ""
             }"""]
        }

        def data = [totalRecords: unidadeInstanceTotal, results: fields]
        render data as JSON
    }

    def generateCartaSenha() {
        def unidadeInstance = Unidade.get(params.id)

        try {
            if (geracaoCartaSenhaService.gerarArquivo(unidadeInstance))
                flash.message = "Arquivo de Carta Senha gerado"
            else
                flash.message = "Nao há cartões para Geração de Carta Senha"
        } catch (Exception e) {
            flash.message = "Erro ao gerar Arquivo de Carta Senha"
            log.error e
        }
        redirect(action: "show", id: unidadeInstance?.id)
    }


}
