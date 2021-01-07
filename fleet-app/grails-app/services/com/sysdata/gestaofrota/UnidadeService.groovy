package com.sysdata.gestaofrota

class UnidadeService {

    synchronized def saveNew(Unidade unidadeInstance) {
        def ultCod = Unidade.withCriteria(uniqueResult: true) {
            projections {
                max("codigo")
            }
        }
        def novoCod = ultCod ? ++(ultCod as long) : 1
        unidadeInstance.codigo = novoCod
        unidadeInstance.save(flush: true)
    }

    def delete(Unidade unidade) {
        def ret = [:]
        ret.success = true
        ret.message = ""
        def cannotDelete = unidade.rh.vinculoCartao == TipoVinculoCartao.FUNCIONARIO && Funcionario.countFuncionariosUnidade(unidade).get() > 0
        if (! cannotDelete) {
            cannotDelete = unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA && MaquinaMotorizada.countMaquinasUnidade(unidade).get() > 0
            if (! cannotDelete) {
                Rh rh = unidade.rh
                rh.removeFromUnidades(unidade)
                unidade.delete(flush: true)
                rh.save(flush: true)
                ret.message = "Unidade #$unidade removida com sucesso"
            }
            else {
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
            }
        } else {
            ret.success = false
            ret.message = "Unidade não pode ser removida, pois já possui funcionários vinculados"
        }
        ret
    }
}
