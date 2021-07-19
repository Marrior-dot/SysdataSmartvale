package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class ProdutoEstabelecimentoService {

    def save(Estabelecimento estabelecimento, params) {

        def ret = [success: true]

        def produtos = params.findAll { it.key ==~ /produto\[\d+\]/ }

        def prodsEstab = ProdutoEstabelecimento.findAllByEstabelecimento(estabelecimento)

        if (produtos) {
            log.info "Vinculando Produtos a Estabelecimento ..."
            produtos.each { k, v ->
                Produto produto = Produto.get(v.produto.id as long)
                ProdutoEstabelecimento prodEstab = prodsEstab.find { it.produto.id == produto.id }

                if (v.ativo && !prodEstab) {
                    new ProdutoEstabelecimento(produto: produto, estabelecimento: estabelecimento).save(flush: true)
                    log.info "\t (+) PRD #${produto.id} a EST #${estabelecimento.id}"
                }
                else if (!v.ativo && prodEstab) {
                    prodEstab.delete(flush: true)
                    log.info "\t (-) PRD #${produto.id} a EST #${estabelecimento.id}"
                }
            }
        }
        return ret
    }
}
