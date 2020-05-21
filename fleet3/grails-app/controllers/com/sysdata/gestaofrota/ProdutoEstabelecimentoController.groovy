package com.sysdata.gestaofrota


class ProdutoEstabelecimentoController {

//    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    static allowedMethods = [save: "POST"]


    def produtoEstabelecimentoService

//    def index() {
//        redirect(action: "list", params: params)
//    }
//
//    def list() {
//        params.max = Math.min(params.max ? params.int('max') : 10, 100)
//        [produtoEstabelecimentoInstanceList: ProdutoEstabelecimento.list(params), produtoEstabelecimentoInstanceTotal: ProdutoEstabelecimento.count()]
//    }
//
//    def create() {
//        [produtoEstabelecimentoInstance: new ProdutoEstabelecimento(params)]
//    }

    def edit() {

        Estabelecimento estabelecimento = Estabelecimento.get(params.long('estId'))
        def produtoList = Produto.list()
        def produtoEstabelecimentoList = ProdutoEstabelecimento.findAllByEstabelecimento(estabelecimento)
        render template: 'form', model: [estabelecimentoInstance   : estabelecimento,
                                     produtoList               : produtoList,
                                     produtoEstabelecimentoList: produtoEstabelecimentoList,
                                     action                    : Util.ACTION_EDIT]

    }


    def save() {
        Estabelecimento estabelecimento = Estabelecimento.get(params.long('estabelecimento.id'))
        List<Produto> produtosSelecionados = params.list('produtosSelecionados').collect { Produto.get(it) }

        ProdutoEstabelecimento.findAllByEstabelecimento(estabelecimento).each {
            it.ativo = false
            produtoEstabelecimentoService.save(it)
        }

        produtosSelecionados.each { produto ->
            ProdutoEstabelecimento produtoEstabelecimento = ProdutoEstabelecimento.findByProdutoAndEstabelecimento(produto, estabelecimento) ?:
                    new ProdutoEstabelecimento(produto: produto, estabelecimento: estabelecimento, valorAnterior: 0D)

            String valorStr = params["valor[${produto.id}]"] ?: "0"
            produtoEstabelecimento.ativo = true
            produtoEstabelecimento.valor = Util.parseCurrency(valorStr)

            if (! produtoEstabelecimento.save(flush: true)){
                log.error produtoEstabelecimento.errors
                flash.message = "Erro ao vincular Produto ao Estabelecimento"
                redirect(controller: 'estabelecimento', action: "show", id: estabelecimento.id)
            }
        }
        flash.message = "Produtos vinculados ao Estabelecimento"
        redirect(controller: 'estabelecimento', action: "show", id: estabelecimento.id)
    }

//    def show() {
//        def produtoEstabelecimentoInstance = ProdutoEstabelecimento.get(params.id)
//        if (!produtoEstabelecimentoInstance) {
//            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produtoEstabelecimento.label', default: 'ProdutoEstabelecimento'), params.id])
//            redirect(action: "list")
//            return
//        }
//
//        [produtoEstabelecimentoInstance: produtoEstabelecimentoInstance]
//    }
//
//    def edit() {
//        def produtoEstabelecimentoInstance = ProdutoEstabelecimento.get(params.id)
//        if (!produtoEstabelecimentoInstance) {
//            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produtoEstabelecimento.label', default: 'ProdutoEstabelecimento'), params.id])
//            redirect(action: "list")
//            return
//        }
//
//        [produtoEstabelecimentoInstance: produtoEstabelecimentoInstance]
//    }
//
//    def update() {
//        def produtoEstabelecimentoInstance = ProdutoEstabelecimento.get(params.id)
//        if (!produtoEstabelecimentoInstance) {
//            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produtoEstabelecimento.label', default: 'ProdutoEstabelecimento'), params.id])
//            redirect(action: "list")
//            return
//        }
//
//        if (params.version) {
//            def version = params.version.toLong()
//            if (produtoEstabelecimentoInstance.version > version) {
//                produtoEstabelecimentoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
//                        [message(code: 'produtoEstabelecimento.label', default: 'ProdutoEstabelecimento')] as Object[],
//                        "Another user has updated this ProdutoEstabelecimento while you were editing")
//                render(view: "edit", model: [produtoEstabelecimentoInstance: produtoEstabelecimentoInstance])
//                return
//            }
//        }
//
//        produtoEstabelecimentoInstance.properties = params
//
//        if (!produtoEstabelecimentoInstance.save(flush: true)) {
//            render(view: "edit", model: [produtoEstabelecimentoInstance: produtoEstabelecimentoInstance])
//            return
//        }
//
//        flash.message = message(code: 'default.updated.message', args: [message(code: 'produtoEstabelecimento.label', default: 'ProdutoEstabelecimento'), produtoEstabelecimentoInstance.id])
//        redirect(action: "show", id: produtoEstabelecimentoInstance.id)
//    }
//
//    def delete() {
//        def produtoEstabelecimentoInstance = ProdutoEstabelecimento.get(params.id)
//        if (!produtoEstabelecimentoInstance) {
//            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produtoEstabelecimento.label', default: 'ProdutoEstabelecimento'), params.id])
//            redirect(action: "list")
//            return
//        }
//
//        try {
//            produtoEstabelecimentoInstance.delete(flush: true)
//            flash.message = message(code: 'default.deleted.message', args: [message(code: 'produtoEstabelecimento.label', default: 'ProdutoEstabelecimento'), params.id])
//            redirect(action: "list")
//        }
//        catch (DataIntegrityViolationException e) {
//            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'produtoEstabelecimento.label', default: 'ProdutoEstabelecimento'), params.id])
//            redirect(action: "show", id: params.id)
//        }
//    }
}
