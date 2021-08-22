package com.sysdata.gestaofrota.proc.cartaoProvisorio

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.Cartao
import com.sysdata.gestaofrota.EnvioResetSenha
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.PortadorFuncionario
import com.sysdata.gestaofrota.RelacaoCartaoPortador
import grails.gorm.transactions.Transactional

@Transactional
class ResetEnvioSenhaCartaoProvisorioService implements ExecutableProcessing {

    def geradorCartao

    @Override
    def execute(Date date) {
        log.info "Recuperando relações cartão/portador p/ reset e envio de senha..."
        def resetSenhaList = RelacaoCartaoPortador.withCriteria {
                                eq("envioResetSenha", EnvioResetSenha.ENVIAR)
                            }
        if (resetSenhaList) {
            log.info "Resetando novas senha p/ cartões..."
            resetSenhaList.each { RelacaoCartaoPortador relacao ->
                log.info "\tCRT #${relacao.cartao.id}"
                resetSenha(relacao.cartao)
                enviarSenha(relacao.portador)
                relacao.envioResetSenha = EnvioResetSenha.ENVIADA
                relacao.save()
            }

        } else
            log.warn "Não há Reset Senha de Cartões Provisórios p/ enviar"
    }

    private void resetSenha(Cartao cartao) {
        cartao.senha = geradorCartao.gerarSenha()
        cartao.save()
    }

    private void enviarSenha(Portador portador) {
        if (portador.instanceOf(PortadorFuncionario)) {
            PortadorFuncionario portadorFuncionario = portador as PortadorFuncionario
            if (portadorFuncionario.funcionario.email) {


            } else
                log.error "FCN #${portadorFuncionario.funcionario.id} não possui email registrado"
        }
    }
}
