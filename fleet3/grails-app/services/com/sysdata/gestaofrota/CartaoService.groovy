package com.sysdata.gestaofrota

class CartaoService {
    def processamentoService
    def geradorCartao

    synchronized Cartao gerar(Portador portador, comChip = true) {
        Administradora administradora = processamentoService.getAdministradoraProjeto()
        Cartao cartaoInstance = new Cartao()
        cartaoInstance.portador = portador
        cartaoInstance.numero = geradorCartao.gerarNumero(administradora, portador)
        cartaoInstance.senha = geradorCartao.gerarSenha()
        cartaoInstance.validade = geradorCartao.gerarDataValidade()
        cartaoInstance.cvv = geradorCartao.gerarCVV()

        if (!cartaoInstance.save()) throw new RuntimeException(cartaoInstance.showErrors())

        portador.addToCartoes(cartaoInstance)
        portador.save()

        administradora.qtdCartoes++
        administradora.save(flush: true)

        return cartaoInstance
    }

    Cartao desbloquear(Cartao cartao) {
       cartao.status = StatusCartao.ATIVO
       cartao.save()
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

        cartao.save()
    }
}