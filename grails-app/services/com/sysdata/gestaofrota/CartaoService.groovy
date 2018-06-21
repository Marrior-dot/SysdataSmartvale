package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.processamento.administradoras.AdministradoraCartao
import com.sysdata.gestaofrota.processamento.administradoras.Sysdata

class CartaoService {
    def processamentoService

    Cartao gerar(Portador portador, comChip = true) {
        AdministradoraCartao administradoraCartao
        Administradora administradora = processamentoService.getAdministradoraProjeto()
        if(comChip){
            administradoraCartao = processamentoService.getAdministradoraCartao()
        }else{
            administradoraCartao = new Sysdata()
        }


        Cartao cartaoInstance = new Cartao()
        cartaoInstance.portador = portador
        cartaoInstance.numero = administradoraCartao.gerarNumero(administradora, portador)
        cartaoInstance.senha = administradoraCartao.gerarSenha()
        cartaoInstance.validade = administradoraCartao.gerarDataValidade()
        cartaoInstance.cvv = administradoraCartao.gerarCVV()

        if (!cartaoInstance.save()) throw new RuntimeException(cartaoInstance.showErrors())

        portador.addToCartoes(cartaoInstance)
        portador.save()
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