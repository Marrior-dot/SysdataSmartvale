package com.sysdata.gestaofrota

import grails.converters.JSON

class EstabelecimentoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def estabelecimentoService


    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def estabelecimentoInstanceList = Estabelecimento.list(params)
        [estabelecimentoInstanceList: estabelecimentoInstanceList, estabelecimentoInstanceTotal: Estabelecimento.count()]
    }

    def create = {
        if (params.empId) {
            PostoCombustivel empresaInstance = PostoCombustivel.get(params.long('empId'))
            render(view: "form", model: [empresaInstance: empresaInstance, action: Util.ACTION_NEW])
        } else {
            flash.message = "Empresa não selecionada!"
            redirect(action: 'list')
        }
    }

    def save = {
        flash.errors = []
        Estabelecimento estabelecimentoInstance = new Estabelecimento(params)
        int estabelecimentoCadastrados = Estabelecimento.countByCnpj(estabelecimentoInstance.cnpj)
        if (estabelecimentoCadastrados > 0) {
            estabelecimentoInstance.errors.rejectValue('cnpj', "Já existe um Estabelecimento cadastrado com o CNPJ ${estabelecimentoInstance.cnpj}")
            render(view: "form", model: [estabelecimentoInstance: estabelecimentoInstance, action: 'novo'])
            return
        }

        estabelecimentoInstance.endereco = params['endereco']
        estabelecimentoInstance.telefone = params['telefone']

        try {
            Estabelecimento.withTransaction {
                if (params.empId) {
                    def empresaInstance = PostoCombustivel.get(params.empId)
                    estabelecimentoInstance.empresa = empresaInstance
                    if (estabelecimentoService.gerarCodigo(estabelecimentoInstance) && estabelecimentoService.save(estabelecimentoInstance)) {
                        flash.message = "${message(code: 'default.created.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), estabelecimentoInstance.nome])}"
                        redirect(action: "show", id: estabelecimentoInstance.id)
                    } else {
                        render(view: "form", model: [estabelecimentoInstance: estabelecimentoInstance, empresaInstance: estabelecimentoInstance.empresa, action: Util.ACTION_NEW])
                    }
                } else {
                    flash.message = "Estabelecimento não relacionado a uma Empresa específica"
                    render(view: "form", model: [estabelecimentoInstance: estabelecimentoInstance, empresaInstance: estabelecimentoInstance.empresa, action: Util.ACTION_NEW])
                }
            }

        } catch (Exception e) {
            flash.errors = e.message
            render(view: "form", model: [estabelecimentoInstance: estabelecimentoInstance, empresaInstance: estabelecimentoInstance.empresa, action: Util.ACTION_NEW])
        }
    }

    def show = {
        def estabelecimentoInstance = Estabelecimento.get(params.long('id'))

        if (!estabelecimentoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), params.id])}"
            redirect(action: "list")
        } else {
            List<Produto> produtoList = Produto.list()
            List<ProdutoEstabelecimento> produtoEstabelecimentoList = ProdutoEstabelecimento.findAllByEstabelecimento(estabelecimentoInstance)
            render(view: 'form', model: [estabelecimentoInstance   : estabelecimentoInstance,
                                         produtoList               : produtoList,
                                         produtoEstabelecimentoList: produtoEstabelecimentoList,
                                         empresaInstance           : estabelecimentoInstance.empresa,
                                         action                    : Util.ACTION_VIEW])
        }
    }

    def edit = {
        def estabelecimentoInstance = Estabelecimento.get(params.long('id'))

        if (!estabelecimentoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), params.id])}"
            redirect(action: "list")
        } else {
            List<Produto> produtoList = Produto.list()
            List<ProdutoEstabelecimento> produtoEstabelecimentoList = ProdutoEstabelecimento.findAllByEstabelecimento(estabelecimentoInstance)
            render view: 'form', model: [estabelecimentoInstance   : estabelecimentoInstance,
                                         produtoList               : produtoList,
                                         produtoEstabelecimentoList: produtoEstabelecimentoList,
                                         action                    : 'editando']
        }
    }

    def update = {
        def estabelecimentoInstance = Estabelecimento.get(params.long('id'))
        if (estabelecimentoInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (estabelecimentoInstance.version > version) {
                    estabelecimentoInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'estabelecimento.label', default: 'Estabelecimento')] as Object[], "Another user has updated this Estabelecimento while you were editing")
                    render(view: "form", model: [estabelecimentoInstance: estabelecimentoInstance, action: 'editando'])
                    return
                }
            }

            Estabelecimento estabelecimentoCadastrado = Estabelecimento.findByCnpj(estabelecimentoInstance.cnpj)
            if (estabelecimentoCadastrado && estabelecimentoCadastrado.id != estabelecimentoInstance.id) {
                estabelecimentoInstance.errors.rejectValue('cnpj', "Já existe um Estabelecimento cadastrado com o CNPJ ${estabelecimentoInstance.cnpj}")
                render(view: "form", model: [estabelecimentoInstance: estabelecimentoInstance, action: 'editando'])
                return
            }


            estabelecimentoInstance.properties = params
            estabelecimentoInstance.endereco = params['endereco']
            estabelecimentoInstance.telefone = params['telefone']

            if (estabelecimentoService.save(estabelecimentoInstance)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), estabelecimentoInstance.id])}"
                redirect(action: "show", id: estabelecimentoInstance.id)
            } else {
                render(view: "form", model: [estabelecimentoInstance: estabelecimentoInstance, action: 'editando'])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def estabelecimentoInstance = Estabelecimento.get(params.id)
        if (estabelecimentoInstance) {
            try {
                estabelecimentoInstance.status = Status.INATIVO
                estabelecimentoInstance.save(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), params.id])}"
            redirect(action: "list")
        }
    }

    def listAllJSON = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def offset = params.start ?: 10
        def empId = params.empId != 'null' ? params.empId.toLong() : null

        def estabelecimentoInstanceList = Estabelecimento.withCriteria {
            eq('status', Status.ATIVO)
            empresa { eq('id', empId) }
        }
        def estabelecimentoInstanceTotal = Estabelecimento
                .createCriteria()
                .list() {
            eq('status', Status.ATIVO)
            empresa { eq('id', empId) }
            projections { rowCount() }
        }

        def fields = estabelecimentoInstanceList.collect { e ->
            [id          : e.id,
             razao       : e.nome,
             nomeFantasia: e.nomeFantasia,
             codEstab    : "<a href='${createLink(controller: 'estabelecimento', action: 'show', id: e.id)}'>${e.codigo}</a>"]
        }

        def data = [recordsTotal: estabelecimentoInstanceTotal, results: fields]
        render data as JSON
    }

    def getByCodigo = {
        def estabelecimentoInstance = Estabelecimento.findByCodigo(params.codigo)
        def data = [razao: estabelecimentoInstance?.empresa?.nome]
        render data as JSON
    }

}
