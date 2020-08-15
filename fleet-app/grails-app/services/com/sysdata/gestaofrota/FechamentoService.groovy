package com.sysdata.gestaofrota

import javax.validation.ValidationException

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
        if (fechamento.cortes.size() > 0) fechamento.ativo = false
        else fechamento.delete()
    }

    def findFaturaByCorte(Corte corte) {

        if (corte.status == StatusCorte.ABERTO) {
            def fatura = [:]
            def totalFatura = LancamentoPortador.withCriteria(uniqueResult: true) {
                                                        projections {
                                                            sum("valor")
                                                        }
                                                        eq("statusFaturamento", StatusFaturamento.NAO_FATURADO)
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
