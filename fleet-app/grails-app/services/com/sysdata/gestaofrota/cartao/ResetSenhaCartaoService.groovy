package com.sysdata.gestaofrota.cartao

import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.cartaoProvisorio.ResetEnvioSenhaCartaoProvisorioService
import grails.gorm.transactions.Transactional

@Transactional
class ResetSenhaCartaoService {

    def springSecurityService
    ResetEnvioSenhaCartaoProvisorioService resetEnvioSenhaCartaoProvisorioService

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
            ResetSenhaCartao resetSenhaCartao
            if (cartao.portador.instanceOf(PortadorMaquina)) {
                PortadorMaquina portadorMaquina = cartao.portador as PortadorMaquina
                if (portadorMaquina.maquina.funcionarios) {
                    resetSenhaCartao = new ResetSenhaCartao(cartao: cartao, solicitante: springSecurityService.currentUser)
                } else {
                    ret.success = false
                    log.error "CRT #${cartao.id} => Não há funcionários vinculados ao Portador Máquina deste cartão!"
                    ret.message = "Não há funcionários vinculados ao Portador deste cartão!"
                    return ret
                }

            } else if (cartao.portador.instanceOf(PortadorFuncionario)) {
                resetSenhaCartao = new ResetSenhaCartao(cartao: cartao, solicitante: springSecurityService.currentUser)
                PortadorFuncionario portadorFuncionario = cartao.portador as PortadorFuncionario
                resetSenhaCartao.addToFuncionarios(portadorFuncionario.funcionario)
            }
            resetSenhaCartao.save(flush: true)
            log.info "(+) Reset Senha #${resetSenhaCartao.id}"
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
