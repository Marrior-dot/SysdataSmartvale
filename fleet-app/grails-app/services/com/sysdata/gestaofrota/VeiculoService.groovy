package com.sysdata.gestaofrota


class VeiculoService {

    PortadorService portadorService
    CartaoService cartaoService

    Veiculo alteraHodometro(Veiculo veiculo, long valor, User usuario) {
        if (valor) {
            def valorAnterior = veiculo.hodometro
            veiculo.hodometro = valor
            veiculo.save(flush: true)
            try {
                HistoricoHodometro historicoHodometro = new HistoricoHodometro(veiculo: veiculo, hodometroAntigo: valorAnterior, hodometroNovo: veiculo.hodometro, user: usuario)
                historicoHodometro.save()
            }
            catch (Exception e) {
                println e
            }

        }
        veiculo
    }

    def save(Veiculo veiculo, Map params) {

        def ret = [success: true]

        if (veiculo.unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA) {
            PortadorMaquina portadorMaquina = portadorService.save(veiculo, params)
            if (portadorMaquina.unidade.rh.cartaoComChip)
                cartaoService.gerar(portadorMaquina)
            else
                cartaoService.gerar(portadorMaquina, false)
        }

        if (! veiculo.save(flush: true)) {
            ret.success = false
            ret.message = veiculo.errors
            return ret
        }

        ret
    }

    def update(Veiculo veiculoInstance, params) {
        if (veiculoInstance.unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA) {
            veiculoInstance.portador.limiteTotal = Util.convertToCurrency(params.portador.limiteTotal)

            if (params.portador.limiteDiario)
                veiculoInstance.portador.limiteDiario = Util.convertToCurrency(params.portador.limiteDiario)

            if (params.portador.limiteMensal)
                veiculoInstance.portador.limiteMensal = Util.convertToCurrency(params.portador.limiteMensal)
        }
        veiculo.save(flush: true)
    }

    def delete(Veiculo veiculo) {
        if (veiculo.portador && veiculo.portador.cartaoAtivo) {

            Cartao cartao = veiculo.portador.cartaoAtivo
            cartao.status = StatusCartao.CANCELADO
            cartao.save()
            log.info "CRT #$cartao.id cancelado"

            veiculo.status = Status.INATIVO
            veiculo.save(flush: true)
            log.info "Veiculo #$veiculo.id inativado"


        } else if (veiculo.portador) {

            Portador portador = veiculo.portador
            def prtId = portador.id
            def vecId = veiculo.id
            portador.delete()
            log.info "PRT #$prtId del"

            veiculo.delete(flush: true)
            log.info "Veiculo #$vecId del"

        } else if (veiculo.funcionarios) {
            veiculo.status = Status.INATIVO
            veiculo.save(flush: true)
            log.info "Veiculo #$veiculo.id inativado"
        } else {
            def vecId = veiculo.id
            veiculo.delete(flush: true)
            log.info "Veiculo #$vecId del"
        }
    }

}
