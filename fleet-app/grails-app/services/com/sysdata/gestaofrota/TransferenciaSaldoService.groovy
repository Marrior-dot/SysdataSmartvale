package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.BusinessException
import grails.gorm.transactions.Transactional

@Transactional
class TransferenciaSaldoService {

    def springSecurityService

    CartaoService cartaoService

    void transferirSaldo(Cartao cartaoOrigem, Cartao cartaoDestino, BigDecimal valor) {
        if (valor > 0) {
            if (cartaoOrigem.portador.unidade == cartaoDestino.portador.unidade) {

                if (cartaoOrigem.status in [StatusCartao.CANCELADO, StatusCartao.BLOQUEADO])
                    throw new BusinessException("Não é possível transferir saldo para um cartão: ${cartaoOrigem.status}")

                if (cartaoDestino.status in [StatusCartao.CANCELADO, StatusCartao.BLOQUEADO])
                    throw new BusinessException("Não é possível transferir saldo para um cartão: ${cartaoDestino.status}")

                if (cartaoOrigem.portador.saldoTotal >= valor) {
                    Transacao transacaoDebito = gerarTransferenciaDebito(cartaoOrigem, valor)
                    Transacao transacaoCredito = gerarTransferenciaCredito(cartaoDestino, valor)

                    HistoricoTransferencia historicoTransferencia = new HistoricoTransferencia()
                    historicoTransferencia.transacaoDebito = transacaoDebito
                    historicoTransferencia.transacaoCredito = transacaoCredito
                    historicoTransferencia.user = springSecurityService.currentUser
                    historicoTransferencia.valorTransferencia = valor
                    historicoTransferencia.save(flush: true)
                } else
                    throw new BusinessException("Valor de transferência não pode ser superior ao valor do saldo do cartão origem!")


            } else
                throw new BusinessException("Cartões Origem e Destino não participam da mesma Unidade!")

        } else
            throw new BusinessException("Valor de transferência não pode ser nulo!")

    }



    private def gerarTransferenciaDebito(Cartao cartaoOrigem, valorDebitar) {

        //Gera transação de Transferência de Débito para Cartão Origem
        Transacao transDebito = new Transacao(
                numeroCartao: cartaoOrigem.numero,
                cartao: cartaoOrigem,
                valor: -valorDebitar,
                dataHoraHost: new Date(),
                dataHoraTerminal: new Date(),
                nsuHost: 0,
                tipo: TipoTransacao.TRANSFERENCIA_SALDO,
                codigoResposta: '00',
                statusTransacaoRede: StatusControleAutorizacao.CONFIRMADA,
                status: StatusTransacao.AGENDADA
        )
        transDebito.save()
        //Debita valor em saldo do cartão
        cartaoService.atualizarSaldo(cartaoOrigem, -valorDebitar)
        return transDebito
    }


    private def gerarTransferenciaCredito(Cartao cartaoDestino, valorCreditar) {
        //Gera transação de Transferência de Crédito para Cartão Destino
        Transacao transCredito = new Transacao(
                numeroCartao: cartaoDestino.numero,
                cartao: cartaoDestino,
                valor: valorCreditar,
                dataHoraHost: new Date(),
                dataHoraTerminal: new Date(),
                nsuHost: 0,
                tipo: TipoTransacao.TRANSFERENCIA_SALDO,
                codigoResposta: '00',
                statusTransacaoRede: StatusControleAutorizacao.CONFIRMADA,
                status: StatusTransacao.AGENDADA
        )
        transCredito.save()
        //Credita valor em saldo do cartão
        cartaoService.atualizarSaldo(cartaoDestino, valorCreditar)
        return transCredito
    }
}