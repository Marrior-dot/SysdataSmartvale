package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class LoteEmbossingService {

    def springSecurityService

    int countCartoesParaEmbossing() {

        return Cartao.withCriteria() {
                    projections {
                        rowCount()
                    }
                    eq("status", StatusCartao.CRIADO)
        }[0]
    }

    List<Cartao> listCartoesParaEmbossing(params) {

        return Cartao.withCriteria() {

                    createAlias("portador", "port")
                    createAlias("port.unidade", "unid")
                    createAlias("unid.rh", "rh")

                    eq("status", StatusCartao.CRIADO)

                    order("rh.id")
                    maxResults(params.max)
                    firstResult(params.offset)
        }
    }

    List<LoteEmbossing> listLotesEmbossing(params) {
        return LoteEmbossing.list(params)
    }

    int countLotesEmbossing() {
        return LoteEmbossing.withCriteria {
                    projections {
                        rowCount()
                    }
                }[0]
    }

    LoteEmbossing createLoteEmbossing() {


        LoteEmbossing loteEmbossing = new LoteEmbossing(usuario: springSecurityService.currentUser)
        loteEmbossing.save(flush: true)
        log.info "Lote Embossing #${loteEmbossing.id} criado"

        def cartoesEmbossar = Cartao.where { status == StatusCartao.CRIADO }

        log.info "Vinculando Cartões ao Lote..."
        cartoesEmbossar.each { crt ->
            crt.status = StatusCartao.LIBERADO_EMBOSSING
            loteEmbossing.addToCartoes(crt)
            log.info "\t(+) CRT #${crt.id}"
        }
        loteEmbossing.save(flush: true)

        return loteEmbossing
    }


}
