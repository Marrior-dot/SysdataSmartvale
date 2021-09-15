package com.sysdata.gestaofrota

class EquipamentoService {
    def cartaoService


    def create(Unidade unidade, props) {
        def ret = [:]
        if (unidade) {
            if (unidade.rh.modeloCobranca == TipoCobranca.PRE_PAGO && unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA &&
                    CategoriaFuncionario.porUnidade(unidade).count() == 0) {
                ret.success = false
                ret.message = "Não existe nenhum Perfil de Recarga definido. É necessário cadastrar um primeiro."
                return ret
            }

            if (TipoEquipamento.count() == 0) {
                ret.success = false
                ret.message = "É necessário criar primeiramente um Tipo de Equipamento."
                return ret
            } else {
                Equipamento equipamento = new Equipamento(props)
                equipamento.unidade = unidade

                if (unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA) {
                    equipamento.portador = new PortadorMaquina()
                    equipamento.portador.unidade = unidade
                }
                ret.equipamento = equipamento
                ret.success = true
                return ret
            }
        } else {
            ret.success = false
            ret.message = "Unidade com ID '${props.unidade?.id}' não encontrada"
            return ret
        }
    }


    def save(Equipamento equipamento, boolean gerarCartao = false) {
        def ret = [success: true]
        if (!equipamento.id && equipamento.unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA) {
            if (! equipamento.save(flush: true)) {
                ret.success = false
                return ret
            }
            PortadorMaquina portadorMaquina = equipamento.portador
            portadorMaquina.save(flush: true)
            if (portadorMaquina.vincularCartao && gerarCartao) {
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

    def update(Equipamento equipamento, props) {
        if (equipamento) {
            equipamento.properties = props
            if (equipamento.unidade.rh.modeloCobranca == TipoCobranca.POS_PAGO && equipamento.unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA)
                equipamento.portador.limiteTotal = Util.parseCurrency(props.portador.limiteTotal)
            equipamento.save(flush: true)
        }
    }
}
