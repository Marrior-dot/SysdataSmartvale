package com.sysdata.gestaofrota

import com.sysdata.commons.notification.EventDispatcherService
import grails.gorm.transactions.Transactional
import org.hibernate.sql.JoinType

@Transactional
class LoteEmbossingService {

    def springSecurityService
    EventDispatcherService eventDispatcherService

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
                    createAlias("port.unidade", "unid", JoinType.LEFT_OUTER_JOIN)
                    createAlias("unid.rh", "rh", JoinType.LEFT_OUTER_JOIN)
                    eq("status", StatusCartao.CRIADO)
                    'in'("tipo", [TipoCartao.PADRAO, TipoCartao.PROVISORIO])
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
        LoteEmbossing loteEmbossing = new LoteEmbossing(usuario: springSecurityService.currentUser, tipo: TipoLoteEmbossing.PADRAO)
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
        eventDispatcherService.publish("loteembossing.criacao", [loteEmbossing: loteEmbossing])

        return loteEmbossing
    }


}
