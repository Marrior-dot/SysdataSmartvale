package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.CartaoService

class TransferenciaSaldoController {

    def index() { }
    def list() {
        //if (params.cartaoOrigem && params.cartaoDestino && params.valorTransferencia) {
               try {
                   println('estou aqui 2')
                   //def dados = params
                   //def valor = Util.parse(dados.valorTransferencia)
                   println('estou aqui 3')
                   cartaoService.transferenciaEntreCartoes(params?.cartaoOrigem, params?.cartaoDestino, params?.valorTransferencia)
                   flash.message = "Transferência concluída!"
                   redirect(action: 'transferencia')
               } catch (e) {
                   log.error e.message
                   flash.error = e.getMessage()
                   redirect(action: 'transferencia', params: params)
               }
        //}else flash.error = "Obrigatório informar todos os Campos!"
    }
}
