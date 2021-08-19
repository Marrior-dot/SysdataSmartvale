package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class CartaoService {
    def processamentoService
    def geradorCartao

    private Cartao gerarCartao(Portador portador, TipoCartao tipoCartao, comChip = true) {
        Administradora administradora = processamentoService.getAdministradoraProjeto()
        Cartao cartaoInstance = new Cartao()
        cartaoInstance.numero = geradorCartao.gerarNumero(administradora, portador)
        cartaoInstance.senha = geradorCartao.gerarSenha()
        cartaoInstance.validade = geradorCartao.gerarDataValidade()
        cartaoInstance.cvv = geradorCartao.gerarCVV()
        cartaoInstance.tipo = tipoCartao

        portador.addToCartoes(cartaoInstance)
        portador.save(flush: true)

        log.info "CRT ${cartaoInstance.numero} gerado"

        administradora.qtdCartoes++
        administradora.save(flush: true)

        return cartaoInstance
    }

    synchronized gerarCartaoProvisorio(comChip = true) {
        return gerarCartao(PortadorAnonimo.unico, TipoCartao.PROVISORIO, comChip)
    }

    synchronized Cartao gerar(Portador portador, comChip = true) {
        return gerarCartao(portador, TipoCartao.PADRAO, comChip)
    }

    Cartao desbloquear(Cartao cartao) {
       cartao.status = StatusCartao.ATIVO
       cartao.save(flush: true)
    }

    Cartao cancelar(Cartao cartao, MotivoCancelamento motivo) {

        cartao.status = StatusCartao.CANCELADO
        cartao.motivoCancelamento = motivo

        final MotivoCancelamento[] motivosGerarNovoCartao = [MotivoCancelamento.ROUBO, MotivoCancelamento.DANIFICADO,
                                                             MotivoCancelamento.EXTRAVIO, MotivoCancelamento.PERDA]

        if (motivosGerarNovoCartao.contains(motivo)) {
            gerar(cartao.portador)
        } else if (motivo == MotivoCancelamento.SOLICITACAO_ADM) {
            //TODO VERIFICAR A VIABILIDADE DE SE COLOCAR UM STATUS EM Conta (CANCELA-LO AQUI).
//            cartao.portador.conta.status = ...
        }

        cartao.save(flush: true)
    }
}