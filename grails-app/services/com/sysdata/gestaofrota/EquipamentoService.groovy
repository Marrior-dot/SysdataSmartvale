package com.sysdata.gestaofrota

class EquipamentoService {
    def portadorService
    def cartaoService

    Equipamento save(Equipamento equipamento, boolean gerarCartao = false) {
        if (equipamento?.unidade == null) throw new RuntimeException("Equipamento precisa ter alguma unidade.")

        if (equipamento.unidade?.rh?.vinculoCartao == TipoVinculoCartao.MAQUINA
                && equipamento.portador == null) {
            equipamento.save()
            PortadorMaquina portadorMaquina = portadorService.save(equipamento)
            if (gerarCartao) cartaoService.gerar(portadorMaquina)
        }

        if (!equipamento.save(flush: true)) throw new RuntimeException("Erro de negocio em equipamento.")
        equipamento
    }
}
