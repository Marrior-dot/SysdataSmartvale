package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.BusinessException
import com.sysdata.gestaofrota.exception.InvalidCurrencyException

class TransferenciaSaldoController {

    TransferenciaSaldoService transferenciaSaldoService

    def index() { }

    def transferir() {
        Cartao cartaoOrigem = Cartao.findByNumero(params.cartaoOrigem)
        if (cartaoOrigem) {
            Cartao cartaoDestino = Cartao.findByNumero(params.cartaoDestino)
            if (cartaoDestino) {
                try {
                    def valorTransferir = Util.parseCurrency(params.valorTransferencia)
                    transferenciaSaldoService.transferirSaldo(cartaoOrigem, cartaoDestino, valorTransferir)
                    flash.success = "Transferência de Saldo realizada com sucesso!"
                } catch (BusinessException be) {
                    flash.error = be.message
                } catch (Exception e) {
                    e.printStackTrace()
                    flash.error = "Erro interno. Contate o suporte"
                }
            } else
                flash.error = "Cartões destino não encontrado na base!"
        } else
            flash.error = "Cartao origem não encontrado na base!"

        render view: 'index'
    }

}
