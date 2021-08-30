package com.sysdata.gestaofrota.cartao

import com.sysdata.gestaofrota.*
import grails.gorm.transactions.Transactional

@Transactional
class ResetSenhaCartaoService {

    def springSecurityService

    def resetSenha(Cartao cartao) {
        def ret = [:]
        if (cartao.status == StatusCartao.ATIVO) {
            def resetNaoProcessado = ResetSenhaCartao.withCriteria {
                eq("cartao", cartao)
                'in'("status", [StatusResetSenhaCartao.RESET_SENHA, StatusResetSenhaCartao.ENVIAR_SENHA])
            }
            if (resetNaoProcessado) {
                log.error "CRT #${cartao.id} => Existe um Reset Senha ainda pendente para este cartão"
                ret.success = false
                ret.message = "Reset inválido: existe um Reset Senha ainda pendente para este cartão!"
                return ret
            }

            ResetSenhaCartao resetSenhaCartao = new ResetSenhaCartao(cartao: cartao, solicitante: springSecurityService.currentUser)
            resetSenhaCartao.save(flush: true)
            log.info "(+) Reset Senha #${resetSenhaCartao.id}"
            if (cartao.portador.instanceOf(PortadorMaquina)) {
                PortadorMaquina portadorMaquina = cartao.portador as PortadorMaquina

                if (portadorMaquina.maquina.funcionarios) {
                    portadorMaquina.maquina.funcionarios.each {
                        resetSenhaCartao.addToFuncionarios(it.funcionario)
                        log.info "\t(+) FCN #${it.funcionario.id}"
                    }
                    resetSenhaCartao.save(flush: true)
                } else {
                    ret.success = false
                    log.error "CRT #${cartao.id} => Não há funcionários vinculados ao Portador Máquina deste cartão!"
                    ret.message = "Reset inválido: Não há funcionários vinculados ao Portador Máquina deste cartão!"
                    return ret
                }

            } else if (cartao.portador.instanceOf(PortadorFuncionario)) {
                PortadorFuncionario portadorFuncionario = cartao.portador as PortadorFuncionario
                resetSenhaCartao.addToFuncionarios(portadorFuncionario.funcionario)
            }
            resetSenhaCartao.save(flush: true)
            log.info "CRT #${cartao.id} => Reset Senha registrado"
            ret.success = true
            ret.message = "Reset de Senha registrado"
            return ret
        } else {
            log.error "CRT #${cartao.id} => Status cartão incompativel: (${cartao.status.nome})"
            ret.success = false
            ret.message = "Reset inválido: status cartão incompatível com esta operação (${cartao.status.nome})"
            return ret
        }
    }
}
