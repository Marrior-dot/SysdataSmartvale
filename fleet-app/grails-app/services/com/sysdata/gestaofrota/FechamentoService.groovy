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

    def delete(Fechamento fechamento) {
        def ret = [:]
        //Se ja possuir cortes, DESATIVE
        def fid = fechamento.id
        if (fechamento.cortes.size() > 0) {
            def corteAberto = fechamento.corteAberto
            if (corteAberto) {
                def lancamentosAbertos = LancamentoCartao.abertosPorCorte(corteAberto).list()
                if (lancamentosAbertos) {
                    log.info "Fechamento #${fechamento.id} tem Corte #${corteAberto.id} com lançamentos abertos"
                    Fechamento proximoFechamento = next(fechamento)
                    CortePortador novoCorte = proximoFechamento.corteAberto
                    // Vincular lançamentos ao Corte aberto do próximo Fechamento
                    log.info "Alterando corte de lançamentos de #${corteAberto.id} para #${novoCorte.id}..."
                    lancamentosAbertos.each { lancamento ->
                        lancamento.corte = novoCorte
                        lancamento.save()
                        log.info "\tLC #${lancamento.id}"
                    }
                }
                corteAberto.status = StatusCorte.CANCELADO
                corteAberto.save()
            }
            fechamento.ativo = false
            fechamento.save(flush: true)
            ret.success = true
            ret.message = "Fechamento com Cortes vinculados. Fechamento #${fechamento.id} inativado."
            return ret
        } else {
            fechamento.delete(flush: true)
            ret.success = true
            ret.message = "Fechamento #${fid} deletado!"
            return ret
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

    Fechamento next(Fechamento fechamentoAtual) {
        def fechamentosList = Fechamento.list([sort: 'diaCorte'])
        Fechamento proximo = fechamentosList.find { it.diaCorte >= fechamentoAtual.diaCorte }
        if (!proximo)
            proximo = fechamentosList[0]
        return proximo
    }

}
