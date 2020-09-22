package com.sysdata.gestaofrota


class VeiculoService {

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

    def save(Veiculo veiculo) {

        def ret = [success: true]

        if (!veiculo.id && veiculo.unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA) {
            PortadorMaquina portadorMaquina = veiculo.portador
            portadorMaquina.save(flush: true)

            if (portadorMaquina.unidade.rh.cartaoComChip)
                cartaoService.gerar(portadorMaquina)
            else
                cartaoService.gerar(portadorMaquina, false)
        }

        if (! veiculo.save(flush: true)) {
            ret.success = false
            return ret
        }

        ret
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
