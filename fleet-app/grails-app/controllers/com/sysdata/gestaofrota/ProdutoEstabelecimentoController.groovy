package com.sysdata.gestaofrota


class ProdutoEstabelecimentoController {

    static allowedMethods = [save: "POST"]

    def produtoEstabelecimentoService

    def edit() {
        Estabelecimento estabelecimento = Estabelecimento.get(params.long('estId'))
        def produtoList = Produto.list(sort: 'id')
        def produtoEstabelecimentoList = ProdutoEstabelecimento.findAllByEstabelecimento(estabelecimento).sort {it.produto.id}
        render template: 'form', model: [estabelecimentoInstance   : estabelecimento,
                                         produtoList               : produtoList,
                                         produtoEstabelecimentoList: produtoEstabelecimentoList,
                                         action                    : Util.ACTION_EDIT]
    }

    def save() {
        Estabelecimento estabelecimento
        if (params.estabelecimento.id) {
            estabelecimento = Estabelecimento.get(params.long('estabelecimento.id'))
            def ret = produtoEstabelecimentoService.save(estabelecimento, params)
            if (ret.success)
                flash.message = "Produtos vinculados ao Estabelecimento"
            else
                flash.error = ret.message
        } else
            flash.error = "ID Estab não informado"

        redirect(controller: 'estabelecimento', action: "show", id: estabelecimento.id)
    }
}
