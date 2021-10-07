package com.sysdata.gestaofrota.proc.cartaoProvisorio

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.commons.notification.SenderMailService
import com.sysdata.gestaofrota.Cartao
import com.sysdata.gestaofrota.Funcionario
import com.sysdata.gestaofrota.PortadorMaquina
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
        //log.info "Recuperando Reset de Senhas registrados..."
        def resetSenhaList = ResetSenhaCartao.findWhere(status: StatusResetSenhaCartao.RESET_SENHA)
        if (resetSenhaList) {
            log.info "Resetando novas senha p/ cartões..."
            resetSenhaList.each { ResetSenhaCartao reset ->
                log.info "\tCRT #${reset.cartao.id}"
                resetSenha(reset.cartao)
                enviarSenha(reset)
            }
        } //else
//            log.warn "Não há Reset Senha de Cartões Provisórios p/ enviar"

//        log.info "Recuperando Envios de Senhas registrados..."
        def envioSenhaList = ResetSenhaCartao.findWhere(status: StatusResetSenhaCartao.ENVIAR_SENHA)
        if (envioSenhaList) {
            envioSenhaList.each { ResetSenhaCartao reset ->
                log.info "\tCRT #${reset.cartao.id}"
                enviarSenha(reset)
            }
        } //else
            //log.warn "Não há Reset Senha de Cartões Provisórios p/ enviar"
    }

    private void resetSenha(Cartao cartao) {
        cartao.senha = geradorCartao.gerarSenha()
        cartao.save(flush: true)
    }

    private void enviarSenha(ResetSenhaCartao resetSenhaCartao) {

        Cartao cartao = resetSenhaCartao.cartao
        if (cartao.portador.instanceOf(PortadorMaquina)) {
            PortadorMaquina portadorMaquina = cartao.portador as PortadorMaquina
            if (portadorMaquina.maquina.funcionarios) {
                portadorMaquina.maquina.funcionarios.each {
                    if (it.funcionario.email) {
                        log.info "\t\tFCN #${it.funcionario.id} =>"
                        senderMailService.sendMessage("reset.senha", [cartao: resetSenhaCartao.cartao, funcionario: it.funcionario])
                    }
                    else
                        throw new BusinessException("CRT #${resetSenhaCartao.cartao.id} => FCN #${it.funcionario.nome} não possui email registrado")

                    resetSenhaCartao.addToFuncionarios(it.funcionario)
                    log.info "\t(+) FCN #${it.funcionario.id}"
                }
            } else
                throw new BusinessException("CRT #${resetSenhaCartao.cartao.id} => Não há Funcionários vinculados ao Portador deste cartão!")


            resetSenhaCartao.status = StatusResetSenhaCartao.SENHA_ENVIADA
            resetSenhaCartao.save(flush: true)
        }
    }



}
