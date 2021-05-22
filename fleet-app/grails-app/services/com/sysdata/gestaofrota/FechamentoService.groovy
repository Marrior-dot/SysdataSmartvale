package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

import javax.validation.ValidationException

@Transactional
class FechamentoService {

    Fechamento save(Fechamento fechamento) {
        if (!fechamento.validate())
            throw new ValidationException(fechamento.showErrors())
        Fechamento fechamentoExistente = Fechamento.findByAtivoAndDiaCorteAndPrograma(true, fechamento.diaCorte, fechamento.programa)
        if (fechamentoExistente)
            throw new ValidationException("Já existe um Fechamento para o dia de corte (${fechamento.diaCorte}) selecionado.")
        fechamento.save(flush: true)
        fechamento
    }

    void delete(Fechamento fechamento) {
        //Se ja possuir cortes, DESATIVE
        def fid = fechamento.id
        if (fechamento.cortes.size() > 0) {
            fechamento.ativo = false
            fechamento.save(flush: true)
            log.info "Fechamento #${fid} inativado!"
        } else {
            fechamento.delete(flush: true)
            log.info "Fechamento #${fid} deletado!"
        }
    }

    def findFaturaByCorte(Corte corte) {

        if (corte.status == StatusCorte.ABERTO) {
            def fatura = [:]
            def totalFatura = LancamentoPortador.withCriteria(uniqueResult: true) {
                                                        projections {
                                                            sum("valor")
                                                        }
                                                        eq("status", StatusLancamento.A_FATURAR)
                                                        eq("corte", corte)
                                                    }
            if (totalFatura) {
                fatura.data = corte.dataPrevista
                fatura.dataVencimento = corte.dataPrevista + corte.fechamento.diasAteVencimento
                fatura.valorTotal = totalFatura
                fatura.itens = []
                fatura.itens << [
                                    "data": corte.dataPrevista,
                                    "descricao": "CONSOLIDAÇÂO DE FATURAS",
                                    "valor": totalFatura
                                ]
                return fatura
            }

        } else
            return Fatura.findByCorte(corte)
    }

}
