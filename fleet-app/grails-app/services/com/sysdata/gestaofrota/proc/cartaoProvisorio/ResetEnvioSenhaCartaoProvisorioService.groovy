package com.sysdata.gestaofrota.proc.cartaoProvisorio

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.commons.notification.SenderMailService
import com.sysdata.gestaofrota.Cartao
import com.sysdata.gestaofrota.EnvioResetSenha
import com.sysdata.gestaofrota.Funcionario
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.PortadorFuncionario
import com.sysdata.gestaofrota.RelacaoCartaoPortador
import com.sysdata.gestaofrota.ResetSenhaCartao
import com.sysdata.gestaofrota.StatusResetSenhaCartao
import com.sysdata.gestaofrota.exception.BusinessException
import grails.gorm.transactions.Transactional

@Transactional
class ResetEnvioSenhaCartaoProvisorioService implements ExecutableProcessing {

    def geradorCartao
    SenderMailService senderMailService

    @Override
    def execute(Date date) {
        log.info "Recuperando Reset de Senhas registrados..."
        def resetSenhaList = ResetSenhaCartao.findWhere(status: StatusResetSenhaCartao.REGISTRADO)
        if (resetSenhaList) {
            log.info "Resetando novas senha p/ cartões..."
            resetSenhaList.each { ResetSenhaCartao reset ->
                log.info "\tCRT #${reset.cartao.id}"
                resetSenha(reset.cartao)
                enviarSenha(reset)
            }
        } else
            log.warn "Não há Reset Senha de Cartões Provisórios p/ enviar"
    }

    private void resetSenha(Cartao cartao) {
        cartao.senha = geradorCartao.gerarSenha()
        cartao.save(flush: true)
    }

    private void enviarSenha(ResetSenhaCartao resetSenhaCartao) {
        if (resetSenhaCartao.funcionarios) {
            resetSenhaCartao.funcionarios.each { Funcionario funcionario ->
                if (funcionario.email) {
                    log.info "\t\tFCN #${funcionario.id} =>"
                    senderMailService.sendMessage("reset.senha", [cartao: resetSenhaCartao.cartao, funcionario: funcionario])
                }
                else
                    log.error "FCN #${funcionario.id} não possui email registrado"
            }
        } else
            throw new BusinessException("CRT #${resetSenhaCartao.cartao.id} => Não há Funcionários vinvulados no registro de Reset de Senha")

    }
}
