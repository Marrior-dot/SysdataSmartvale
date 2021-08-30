package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
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

    def save(Veiculo veiculo, boolean gerarCartao = false) {

        def ret = [success: true]

        if (!veiculo.id && veiculo.unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA) {

            if (! veiculo.save(flush: true)) {
                ret.success = false
                return ret
            }

            PortadorMaquina portadorMaquina = veiculo.portador
            portadorMaquina.save(flush: true)

            if (portadorMaquina.vincularCartao) {
                if (gerarCartao) {
                    if (portadorMaquina.unidade.rh.cartaoComChip)
                        cartaoService.gerar(portadorMaquina)
                    else
                        cartaoService.gerar(portadorMaquina, false)
                }
            }

        }

        if (! veiculo.save(flush: true)) {
            ret.success = false
            return ret
        }

        ret
    }

    def delete(Veiculo veiculo) {
        def transacaoCount = Transacao.withCriteria(uniqueResult: true) {
                                projections {
                                    rowCount()
                                }
                                eq("maquina", veiculo)
                            }
        if (transacaoCount > 0) {
            Cartao cartao = veiculo.portador.cartaoAtivo
            cartao.status = StatusCartao.CANCELADO
            cartao.save()
            log.info "CRT #$cartao.id cancelado"
            veiculo.status = Status.INATIVO
            veiculo.save(flush: true)
            log.info "Veiculo #$veiculo.id inativado"
        } else if (veiculo.portador) {
            Portador portador = veiculo.portador
            if (portador.cartoes) {
                portador.cartoes.each { crt ->
                    HistoricoStatusCartao.executeUpdate("delete from HistoricoStatusCartao hsc where hsc.cartao =:crt", [crt: crt])
                    log.info "(-) CRT #${crt.id}"
                    crt.delete()
                }
            }
            def prtId = portador.id
            def vecId = veiculo.id
            portador.delete()
            log.info "(-) PRT #$prtId"
            veiculo.delete(flush: true)
            log.info "(-) VCL #$vecId"
        } else if (veiculo.funcionarios) {
            def funcIds = veiculo.funcionarios*.id
            def vecId = veiculo.id
            funcIds.each { fid ->
                MaquinaFuncionario maquinaFuncionario = MaquinaFuncionario.get(fid)
                veiculo.removeFromFuncionarios(maquinaFuncionario)
                maquinaFuncionario.delete(flush: true)
            }
            veiculo.delete(flush: true)
            log.info "(-) VCL #$vecId"
        } else {
            def vecId = veiculo.id
            veiculo.delete(flush: true)
            log.info "(-) VCL #$vecId"
        }
    }

}
