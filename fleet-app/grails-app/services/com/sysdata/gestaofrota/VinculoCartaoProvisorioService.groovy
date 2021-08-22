package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.Cartao
import com.sysdata.gestaofrota.CartaoService
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.PortadorAnonimo
import com.sysdata.gestaofrota.RelacaoCartaoPortador
import com.sysdata.gestaofrota.StatusCartao
import com.sysdata.gestaofrota.StatusRelacaoCartaoPortador
import com.sysdata.gestaofrota.TipoCartao
import com.sysdata.gestaofrota.exception.BusinessException
import grails.gorm.transactions.Transactional

@Transactional
class VinculoCartaoProvisorioService {

    CartaoService cartaoService

    def linkToCardHolder(String cardNumber, Portador cardHolder, Date limitDate) {
        cardNumber = cardNumber.replaceAll("\\s", "")
        Cartao card = Cartao.findWhere(numero: cardNumber)
        def rules = [
            [condition: { it.card != null }, reject: "Cartão '${cardNumber}' não encontrado!"],
            [condition: { it.card.tipo == TipoCartao.PROVISORIO }, reject: "Cartão '${cardNumber}' não do tipo Provisório!"],
            [condition: { it.cardHolder.cartaoAtual?.status != StatusCartao.ATIVO }, reject: "Portador possui um cartão já ativo no momento!"],
            [condition: { it.card.portador == PortadorAnonimo.unico }, reject: "Cartão provisório vinculado a outro portador!"],
            [condition: { it.card.status == StatusCartao.EMBOSSING || it.card.status == StatusCartao.DESVINCULADO }, reject: "Cartão ainda não disponível para vínculo!"],
            [condition: { it.limitDate > new Date().clearTime() }, reject: "Data Limite inválida!"],
        ]
        def ret = [:]
        def pars = [card: card, cardHolder: cardHolder, limitDate: limitDate]

        // Teste de cada regra
        rules.find { rule ->
            ret.ok = rule.condition(pars)
            println "${rule.reject} => ${ret.ok}"
            if (!ret.ok) {
                ret.reject = rule.reject
                return true
            }
        }
        if (ret.ok) {
            card.portador = cardHolder
            card.status = StatusCartao.ATIVO
            card.addToRelacaoPortador(new RelacaoCartaoPortador(portador: cardHolder))
            card.save(flush: true)

        } else
            throw new BusinessException(ret.reject)
    }

    def unlinkFromCardHolder(Cartao card, Portador cardHolder) {
        if (card.tipo == TipoCartao.PROVISORIO) {
            PortadorAnonimo portadorAnonimo = PortadorAnonimo.unico
            card.portador = portadorAnonimo
            log.info "PRT #${cardHolder.id}: (-) CRT #${card.id}"

            RelacaoCartaoPortador relacaoCartaoPortador = card.getRelacaoPortadorAtiva(cardHolder)
            if (! relacaoCartaoPortador)
                throw new BusinessException("Não existe Relação Cartão #${card.id} e Portador #${cardHolder.id} registrada!")

            relacaoCartaoPortador.status = StatusRelacaoCartaoPortador.FINALIZADA_COMANDO
            relacaoCartaoPortador.dataFim = new Date()
            relacaoCartaoPortador.save(flush: true)

/*
            // Depois da desvinculação do cartão provisório, verifica se último cartão está cancelado para gerar um novo cartão
            Cartao lastCard = Cartao.withCriteria(uniqueResult: true) {
                                eq("portador", cardHolder)
                                order("dateCreated", "desc")
                                firstResult(1)

                            }
*/
            Cartao lastCard = cardHolder.cartaoAtual
            if (lastCard.status == StatusCartao.CANCELADO) {
                log.info "PRT #${cardHolder.id}: CRT #${lastCard.id} ${lastCard.status}"
                Cartao newCard = cartaoService.gerar(cardHolder)
                log.info "PRT #${cardHolder.id}: (+) CRT #${newCard.id}"
            }

        } else
            throw new BusinessException("Não pode desvincular um cartão padrão do portador!")
    }


}
