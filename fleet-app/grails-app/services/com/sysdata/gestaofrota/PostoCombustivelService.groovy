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

    def delete(PostoCombustivel empresa) {
        def empId = empresa.toString()
        def transacaoCount = Transacao.withCriteria {
                                projections {
                                    rowCount('id')
                                }
                                estabelecimento {
                                    eq("empresa", empresa)
                                }
                            }[0]
        if (transacaoCount > 0) {
            empresa.status = Status.INATIVO
            empresa.save(flush: true)
            def msgs = []
            msgs << "Conveniado tem transações relacionadas. Conveniado #${empId} inativado"

            empresa.estabelecimentos.each { est ->
                est.status = Status.INATIVO
                est.save(flush: true)
                msgs << "Estabelecimento #${est.id} inativado"
            }
            return [success: true, message: msgs]
        } else {
            empresa.delete(flush: true)
            return [success: true, message: "Conveniado '${empId}' removido"]
        }
    }
}
