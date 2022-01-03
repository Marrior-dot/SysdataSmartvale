package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

import com.sysdata.gestaofrota.Cartao
import com.sysdata.gestaofrota.Transacao
import com.sysdata.gestaofrota.StatusCartao
import org.apache.poi.xssf.usermodel.XSSFWorkbook


@Transactional
class TranferenciaSaldoService {
 private static final int START_LINE = 1

    private def gerarTransferenciaDebito(Cartao cartaoOrigem, valorDebitar) {
        //Gera transação de Transferência de Débito para Cartão Origem
        Transacao transDebito = new Transacao(
                numeroCartao: cartaoOrigem.numero,
                cartao: cartaoOrigem,
                valor: -valorDebitar,
                dataHoraHost: new Date(),
                dataHoraTerminal: new Date(),
                nsuHost: 0,
                tipo: TipoTransacao.TRANSF_SALDO_DEBITO,
                codigoResposta: '00',
                statusTransacaoRede: StatusTransacaoRede.CONFIRMADA,
                statusTransacaoProcessamento: StatusTransacaoProcessamento.NAO_AGENDAR
        )
        transDebito.save()
        //Debita valor em saldo do cartão
        cartaoOrigem.debitarValor(valorDebitar)

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
                tipo: TipoTransacao.TRANSF_SALDO_CREDITO,
                codigoResposta: '00',
                statusTransacaoRede: StatusTransacaoRede.CONFIRMADA,
                statusTransacaoProcessamento: StatusTransacaoProcessamento.NAO_AGENDAR
        )
        transCredito.save()
        //Credita valor em saldo do cartão
        cartaoDestino.creditarValor(valorCreditar)
        cartaoDestino.save()
        return transCredito
    }
}