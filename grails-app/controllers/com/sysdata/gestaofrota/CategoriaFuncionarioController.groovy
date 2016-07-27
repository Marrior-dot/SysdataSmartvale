package com.sysdata.gestaofrota

import grails.converters.JSON
import grails.plugins.springsecurity.Secured


@Secured(['IS_AUTHENTICATED_FULLY'])
class CategoriaFuncionarioController {

    def categoriaFuncionarioService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        redirect(controller: 'rh', action: 'show', params: [categId: params.rhId])
    }

    def create = {
        def rhInstance = Rh.get(params.rhId)
        render(view: 'form', model: [action: Util.ACTION_NEW, rhInstance: rhInstance])
    }

    def save = {
        if (params.rhId) {
            def rhId = params.rhId
            def categoriaFuncionarioInstance = new CategoriaFuncionario(params)
            def rhInstance = Rh.get(rhId)
            categoriaFuncionarioInstance.rh = rhInstance
            if (categoriaFuncionarioInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'categoriaFuncionario.label', default: 'Categoria'), categoriaFuncionarioInstance.id])}"
                redirect(action: "show", id: categoriaFuncionarioInstance.id)
            } else {
                render(view: "form", model: [categoriaFuncionarioInstance: categoriaFuncionarioInstance, rhInstance: categoriaFuncionarioInstance.rh, action: Util.ACTION_NEW])
            }
        } else {
            flash.message = "ERRO INTERNO: Unidade não relacionada a um Rh específico!"
            redirect(action: "list")
        }
    }

    def show = {
        def categoriaFuncionarioInstance = CategoriaFuncionario.get(params.id)
        if (!categoriaFuncionarioInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'categoriaFuncionario.label', default: 'CategoriaFuncionario'), params.id])}"
            redirect(action: "list", params: [rhId: params.rhId])
        } else {
            render(view: "form", model: [categoriaFuncionarioInstance: categoriaFuncionarioInstance, rhInstance: categoriaFuncionarioInstance.rh, action: Util.ACTION_VIEW])
        }
    }

    def edit = {
        def categoriaFuncionarioInstance = CategoriaFuncionario.get(params.id)
        if (!categoriaFuncionarioInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'categoriaFuncionario.label', default: 'CategoriaFuncionario'), params.id])}"
            redirect(action: "list", params: [rhId: params.rhId])
        } else {
            render(view: "form", model: [categoriaFuncionarioInstance: categoriaFuncionarioInstance, rhInstance: categoriaFuncionarioInstance.rh, action: Util.ACTION_EDIT])
        }
    }

    def update = {
        def categoriaFuncionarioInstance = CategoriaFuncionario.get(params.id)
        if (categoriaFuncionarioInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (categoriaFuncionarioInstance.version > version) {

                    categoriaFuncionarioInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'categoriaFuncionario.label', default: 'CategoriaFuncionario')] as Object[], "Another user has updated this CategoriaFuncionario while you were editing")
                    render(view: "form", model: [categoriaFuncionarioInstance: categoriaFuncionarioInstance, rhInstance: categoriaFuncionarioInstance.rh, action: Util.ACTION_EDIT])
                    return
                }
            }
            categoriaFuncionarioInstance.properties = params
            if (!categoriaFuncionarioInstance.hasErrors() && categoriaFuncionarioInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'categoriaFuncionario.label', default: 'CategoriaFuncionario'), categoriaFuncionarioInstance.id])}"
                redirect(action: "show", id: categoriaFuncionarioInstance.id)
            } else {
                render(view: "form", model: [categoriaFuncionarioInstance: categoriaFuncionarioInstance, rhInstance: categoriaFuncionarioInstance.rh, action: Util.ACTION_EDIT])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'categoriaFuncionario.label', default: 'CategoriaFuncionario'), params.id])}"
            redirect(action: "list", params: [rhId: params.rhId])
        }
    }

    def delete = {
        def categoriaFuncionarioInstance = CategoriaFuncionario.get(params.id)
        if (categoriaFuncionarioInstance) {
            try {
                categoriaFuncionarioInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'categoriaFuncionario.label', default: 'CategoriaFuncionario'), params.id])}"
                redirect(action: "list", params: [rhId: params.rhId])
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'categoriaFuncionario.label', default: 'CategoriaFuncionario'), params.id])}"
                redirect(action: "show", model: [categId: categoriaFuncionarioInstance.id])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'categoriaFuncionario.label', default: 'CategoriaFuncionario'), params.id])}"
            redirect(action: "list")
        }
    }

    def listAllJSON = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def offset = params.offset ?: 10
        def opcao
        def filtro
        def rhId = params.rhId ? params.rhId.toLong() : null

        def categoriaInstanceList = CategoriaFuncionario
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
            order("id")
        }
        def categoriaInstanceTotal = CategoriaFuncionario
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
        }

        def fields = categoriaInstanceList.collect { c ->
            [id    : c.id,
             codigo: c.id,
             nome  : c.nome,
             valor : Util.formatCurrency(c.valorCarga),
             acoes : "<a class='show' href='${createLink(action: 'show', id: c.id)}'></a>"]
        }

        def data = [totalRecords: categoriaInstanceTotal, results: fields]
        render data as JSON
    }

    def filter() {
        render (template: "list", model: categoriaFuncionarioService.filter(params))
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def salvarCategoria(){
        log.debug("salvando categoriaa....")
        def retorno = [:]
        def nome = params.categoriaNome;
        def valor = params.categoriaValor.replace("R\$ ", "") as Double;
        def categoriaId = params.categoriaId ? params.categoriaId as Long: 0
        def prg = Rh.get(params.prgId as Long)
        def categoria = null

        if(categoriaId && categoriaId > 0){
            categoria = CategoriaFuncionario.get(categoriaId)
            categoria.nome = nome
            categoria.valorCarga = valor
            if(categoria.save(flush: true)){
                retorno.mensagem = "Atualizado com sucesso"
            } else {
                retorno.mensagem = "Erro ao atualizar"
            }
            retorno.id = categoriaId
        } else {
            categoria = new CategoriaFuncionario(nome: nome, valorCarga: valor)
            prg.addToCategoriasFuncionario(categoria)
            if(prg.save(flush: true)){
                retorno.mensagem = "Salvo com sucesso"
            } else {
                retorno.mensagem = "Erro ao salvar"
            }
            retorno.id = categoria?.id ?:0
        }


        render retorno as JSON
    }

    def carregarCategorias(){
        def categorias = Rh.get(params.prgId as Long).categoriasFuncionario
        render template: 'tabelaCategoria', model: [categorias: categorias.sort{it.nome}]
    }

    def excluirCategoria(){
        def retorno = [:]
        def categoriaId = params.categoriaId as Long
        def prg = Rh.get(params.prgId as Long)
        def categoria = CategoriaFuncionario.get(categoriaId as Long)
        log.debug("CAT:"+categoriaId)
        if(categoria.delete(flush: true)){
            retorno.mensagem = "Sucesso"
        }

        render retorno as JSON
    }

    def cancelarCategoria(){
        def retorno = [:]
        def categoriaId = params.categoriaId as Long
        def categoria = CategoriaFuncionario.get(categoriaId as Long)
        retorno.nome = categoria.nome
        retorno.valorCarga = categoria.valorCarga

        render retorno as JSON

    }
}
