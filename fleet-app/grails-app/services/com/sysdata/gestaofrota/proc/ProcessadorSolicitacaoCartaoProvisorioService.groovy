package com.sysdata.gestaofrota.proc

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.Cartao
import com.sysdata.gestaofrota.CartaoService
import com.sysdata.gestaofrota.LoteEmbossing
import com.sysdata.gestaofrota.SolicitacaoCartaoProvisorio
import com.sysdata.gestaofrota.StatusCartao
import com.sysdata.gestaofrota.StatusSolicitacaoCartaoProvisorio
import com.sysdata.gestaofrota.TipoCartao
import com.sysdata.gestaofrota.TipoLoteEmbossing
import grails.gorm.transactions.Transactional

@Transactional
class ProcessadorSolicitacaoCartaoProvisorioService implements ExecutableProcessing {

    CartaoService cartaoService

    @Override
    def execute(Date date) {

        def solicitacaoList = SolicitacaoCartaoProvisorio.withCriteria {
            eq("status", StatusSolicitacaoCartaoProvisorio.CRIADA)
            order("dateCreated")
        }
        if (solicitacaoList) {
            solicitacaoList.each { SolicitacaoCartaoProvisorio sol ->
                log.info "Processando Solicitação #${sol.id}..."
                def qtdeCartoes = sol.quantidade
                qtdeCartoes.times {
                    cartaoService.gerarCartaoProvisorio()
                    if (it % 50 == 0)
                        clearSession()
                }
                if (!sol.isAttached())
                    sol.attach()
                sol.status = StatusSolicitacaoCartaoProvisorio.PROCESSADA
                sol.save(flush: true)
                log.info "Solicitação #${sol.id} processada"
            }
            gerarLoteEmbossingCartoesProvisorios()
        } else
            log.warn "Nao há Solicitações de Cartões Provisórios a processar"
    }

    private void gerarLoteEmbossingCartoesProvisorios() {
        def cartoesIds = Cartao.withCriteria {
                            projection {
                                property("id")
                            }
                            eq("tipo", TipoCartao.PROVISORIO)
                            eq("status", StatusCartao.LIBERADO_EMBOSSING)
                        }
        if (cartoesIds) {
            LoteEmbossing loteEmbossing = new LoteEmbossing(tipo: TipoLoteEmbossing.PROVISORIO)
            log.info "Criando Lote Embossing Cartões Provisórios..."
            cartoesIds.each { cid, idx ->
                Cartao cartaoProvisorio = Cartao.get(cid)
                loteEmbossing.addToCartoes(cartaoProvisorio)
                log.info "\t(+) CRT ${cid}"
            }
            loteEmbossing.save(flush: true)
            log.info "Lote Embossing #${loteEmbossing.id} criado"
        }
    }

}
