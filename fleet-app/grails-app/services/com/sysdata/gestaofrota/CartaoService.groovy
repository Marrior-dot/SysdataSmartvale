package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.HistoricoTransferencia
import com.sysdata.gestaofrota.exception.BusinessException
import grails.gorm.transactions.Transactional

@Transactional
class CartaoService {
    def processamentoService
    def geradorCartao
    def springSecurityService

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
        Portador portador = cartao.portador
        Cartao cartaoExtraAtivo = portador.cartoes.find { it.tipo == TipoCartao.PROVISORIO && it.status == StatusCartao.ATIVO }
        if (cartaoExtraAtivo)
            throw new BusinessException("Portador possui um cartão Provisório vinculado! Desvincule-o, primeiramente, antes de ativar este cartão")

        if (cartao.status == StatusCartao.EMBOSSING) {
            cartao.status = StatusCartao.ATIVO
            cartao.save(flush: true)
        } else
            throw new BusinessException("Cartão não pode ser ativo! Status inválido para esta operação: ${cartao.status.nome}")

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

    def transferenciaEntreCartoes(numeroOrigem, numeroDestino, valor) {

       log.info "Iniciando transferencia..."

       if (!numeroOrigem || !numeroDestino || !valor) throw new RuntimeException("Parâmetros inválidos")

       def cartaoOrigem = Cartao.findByNumero(numeroOrigem) //criar Consulta dentro de Cartões
       //log.info "CartãoOrigem consultado..."
       if (!cartaoOrigem) throw new RuntimeException("Cartão Origem não encontrado")
       //log.info "CartãoOrigem consultado 2..."
       def cartaoDestino = Cartao.findByNumero(numeroDestino) //criar Consulta dentro de Cartões
       if (!cartaoDestino) throw new RuntimeException("Cartão Destino não encontrado")
       if (cartaoDestino.status in [StatusCartao.CANCELADO, StatusCartao.BLOQUEADO]) throw new RuntimeException("Não é possível transferir saldo para um cartão: ${cartaoDestino.status}")

       if (valor <= new BigDecimal(0)) throw new RuntimeException("Valor da transferencia tem que ser positivo")

       if (valor > cartaoOrigem?.saldo) throw new RuntimeException("O valor da transferencia é maior que o disponível no cartão de origem")

       log.info "Crt Org #${cartaoOrigem.id} - Crt Dst #${cartaoDestino.id} - valor: ${valor}"

       def transDebito = transferenciaService.gerarTransferenciaDebito(cartaoOrigem, valor)
       def transCredito = transferenciaService.gerarTransferenciaCredito(cartaoDestino, valor)

       new HistoricoTransferencia(user: springSecurityService?.getCurrentUser(), transacaoCredito: transCredito,
              transacaoDebito: transDebito, valorTransferencia: valor).save(flush: true, failOnError: true)

    }
}