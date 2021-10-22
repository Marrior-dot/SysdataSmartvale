package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.cartao.ResetSenhaCartaoService
import com.sysdata.gestaofrota.exception.BusinessException
import grails.gorm.transactions.Transactional

@Transactional
class VinculoCartaoProvisorioService {

    CartaoService cartaoService
    ResetSenhaCartaoService resetSenhaCartaoService

    def linkToCardHolder(String cardNumber, Portador cardHolder, Date limitDate) {
        cardNumber = cardNumber.replaceAll("\\s", "")
        Cartao card = Cartao.findWhere(numero: cardNumber)

        def rules = [
            [condition: { it.card != null }, reject: "Cartão '${cardNumber}' não encontrado!"],
            [condition: { it.card.tipo == TipoCartao.PROVISORIO }, reject: "Cartão '${cardNumber}' não do tipo Provisório!"],
            [condition: { it.cardHolder.cartaoAtivo == null }, reject: "Portador já possui um cartão ativo no momento!"],
            [condition: { it.card.portador == PortadorAnonimo.unico }, reject: "Cartão provisório vinculado a outro portador!"],
            [condition: { it.card.status == StatusCartao.EMBOSSING || it.card.status == StatusCartao.DESVINCULADO }, reject: "Cartão ainda não disponível para vínculo!"],
            [condition: { it.limitDate > new Date().clearTime() }, reject: "Data Limite inválida!"],

            [condition: {
                if (it.cardHolder.instanceOf(PortadorMaquina)) {
                    PortadorMaquina portadorMaquina = it.cardHolder as PortadorMaquina
                    return portadorMaquina.maquina.funcionarios.size() > 0
                }
            }, reject: "Portador sem Funcionário(s) vinculado(s)"],

            [condition: {
                if (it.cardHolder.instanceOf(PortadorMaquina)) {
                    PortadorMaquina portadorMaquina = it.cardHolder as PortadorMaquina
                    def funcionarios = portadorMaquina.maquina.funcionarios*.funcionario
                    return ! funcionarios.any { !it.email || it.email.trim() == ""}
                } else if (it.cardHolder.instanceOf(PortadorFuncionario)) {
                    PortadorFuncionario portadorFuncionario = it.cardHolder as PortadorFuncionario
                    return portadorFuncionario.funcionario.email != null
                }
            }, reject: "Existe(m) funcionário(s) vinculado(s) que não possue(m) email definido no respectivo cadastro!"],

            [condition: {
                def extraCard = it.card
                def countCards = RelacaoCartaoPortador.createCriteria().count({
                                    eq("cartao", extraCard)
                                    eq("status", StatusRelacaoCartaoPortador.ATIVA)
                                })
                return countCards == 0
            }, reject: "Cartão já possui vínculo Ativo com portador!"]
        ]
        def ret = [:]
        def pars = [card: card, cardHolder: cardHolder, limitDate: limitDate]

        // Teste de cada regra
        rules.find { rule ->
            ret.ok = rule.condition(pars)
            if (!ret.ok) {
                ret.reject = rule.reject
                return true
            }
        }
        if (ret.ok) {
            card.portador = cardHolder
            card.status = StatusCartao.ATIVO
            card.addToRelacaoPortador(new RelacaoCartaoPortador(portador: cardHolder, dataInicio: new Date()))
            card.save(flush: true)
            def resp = resetSenhaCartaoService.resetSenha(card)
            if (!resp.success)
                throw new BusinessException(resp.message)
        } else
            throw new BusinessException(ret.reject)
    }

    def unlinkFromCardHolder(Cartao card, Portador cardHolder) {
        if (card.tipo == TipoCartao.PROVISORIO) {
            PortadorAnonimo portadorAnonimo = PortadorAnonimo.unico
            card.portador = portadorAnonimo
            card.status = StatusCartao.DESVINCULADO
            log.info "PRT #${cardHolder.id}: (-) CRT #${card.id}"

            RelacaoCartaoPortador relacaoCartaoPortador = card.getRelacaoPortadorAtiva(cardHolder)
            if (! relacaoCartaoPortador)
                throw new BusinessException("Não existe Relação Cartão #${card.id} e Portador #${cardHolder.id} registrada!")

            relacaoCartaoPortador.status = StatusRelacaoCartaoPortador.FINALIZADA_COMANDO
            relacaoCartaoPortador.dataFim = new Date()
            relacaoCartaoPortador.save(flush: true)

            //Caso exista um Reset registrado,
            def resetsSenhasList = ResetSenhaCartao.withCriteria {
                                        eq("cartao", card)
                                        eq("status", StatusResetSenhaCartao.RESET_SENHA)
                                    }
            resetsSenhasList.each { reset ->

                reset.funcionarios.each { func ->
                    reset.removeFromFuncionarios(func)
                }

                log.info "(-) RST #${reset.id}"
                reset.delete(flush: true)
            }


        } else
            throw new BusinessException("Não pode desvincular um cartão padrão do portador!")
    }


}
