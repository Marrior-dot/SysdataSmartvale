package com.sysdata.gestaofrota

class UnidadeService {

    PortadorService portadorService

    synchronized def saveNew(Unidade unidadeInstance) {
        def ultCod = Unidade.executeQuery("select max(cast(codigo as integer )) from Unidade")[0]
        def novoCod = ultCod ? ++ultCod : 1
        unidadeInstance.codigo = novoCod
        unidadeInstance.save(flush: true)
    }


    def delete(Unidade unidade) {
        log.info "Excluindo Unidade #${unidade.id}..."
        def ret = [:]
        ret.success = true
        ret.message = ""
        def cannotDelete = Transacao.countTransacoesUnidade(unidade).get() > 0 /*&& Cartao.countCartoesAtivosUnidade(unidade).get() > 0

        if (! cannotDelete) {
            cannotDelete = (unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA && MaquinaMotorizada.countMaquinasUnidade(unidade).get() > 0) ||
                                (unidade.rh.vinculoCartao == TipoVinculoCartao.FUNCIONARIO && Funcionario.countFuncionariosUnidade(unidade).get() > 0)
*/
            if (! cannotDelete) {
                def portadoresIds = Portador.withCriteria {
                                        projections {
                                            property "id"
                                        }
                                        eq("unidade", unidade)
                                    }
                portadoresIds.eachWithIndex { pid, idx ->
                    Portador portador = Portador.get(pid)
                    portadorService.delete(portador)
                    if ((idx + 1) % 50 == 0)
                        clearSession()
                }
                if (! unidade.isAttached())
                    unidade.attach()

                Rh rh = unidade.rh
                rh.removeFromUnidades(unidade)
                log.info "(-) Unidade #${unidade.id}"
                unidade.delete(flush: true)
                rh.save(flush: true)
                ret.message = "Unidade #$unidade removida com sucesso"
            }

            else {

                ret.success = false
                ret.message = "Unidade não pode ser removida, pois já possui cartões transacionando"



/*
                if (unidade.status != Status.INATIVO)  {
                    unidade.status = Status.INATIVO

                    //TODO: Cancelar cartões, portadores, funcionários e máquinas
                    unidade.save(flush: true)
                    ret.success = false
                    ret.message = "Unidade #$unidade inativada. Não pode ser removida, pois já possui Funcionários/Máquinas"
                } else {
                    ret.success = false
                    ret.message = "Unidade #$unidade já inativada. Não pode ser removida, pois já possui Funcionários/Máquinas"
                }
*/
            }
        ret
    }
}
