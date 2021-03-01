package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class PostoCombustivelService {

    synchronized gerarCodigo(postoCombustivelInstance) {
        def ultCod = Estabelecimento.withCriteria {
                        projections { max('codigo') }
                    }[0] ?: 0

        ultCod = ultCod.toLong()
        def novoCodEstab = String.format("%015d", ++ultCod)
        log.debug("Novo Cod Estab:${novoCodEstab}")

        postoCombustivelInstance.addToEstabelecimentos(new Estabelecimento(codigo: novoCodEstab))

        postoCombustivelInstance.save(flush: true)

    }


}
