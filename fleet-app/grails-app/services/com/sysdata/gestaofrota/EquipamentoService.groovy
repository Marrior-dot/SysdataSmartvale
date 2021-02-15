package com.sysdata.gestaofrota

class EquipamentoService {
    def cartaoService

    def save(Equipamento equipamento, boolean gerarCartao = false) {
        def ret = [success: true]

        if (!equipamento.id && equipamento.unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA) {

            if (! equipamento.save(flush: true)) {
                ret.success = false
                return ret
            }

            PortadorMaquina portadorMaquina = equipamento.portador
            portadorMaquina.save(flush: true)

            if (gerarCartao) {
                if (portadorMaquina.unidade.rh.cartaoComChip)
                    cartaoService.gerar(portadorMaquina)
                else
                    cartaoService.gerar(portadorMaquina, false)
            }
        }

        if (! equipamento.save(flush: true)) {
            ret.success = false
            return ret
        }

        ret
    }
}
