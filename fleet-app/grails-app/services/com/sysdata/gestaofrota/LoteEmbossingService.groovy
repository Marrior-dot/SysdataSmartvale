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

    List<Cartao> listCartoesParaEmbossing(int max, int offset) {

        return Cartao.withCriteria(max: max, offset: offset) {

                    createAlias("portador", "port")
                    createAlias("port.unidade", "unid")
                    createAlias("unid.rh", "rh")

                    eq("status", StatusCartao.CRIADO)

                    order("rh.id")
        }
    }

    List<LoteEmbossing> listLotesEmbossing(int max, int offset) {
        return LoteEmbossing.list([max: max, offset: offset, order: "dateCreated"])
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
