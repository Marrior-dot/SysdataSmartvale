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
                unidade.delete(flush: true)
                ret.message = "Unidade #$unidade.id removida com sucesso"
            }
            else {
                unidade.status = Status.INATIVO
                unidade.save(flush: true)
                ret.success = false
                ret.message = "Unidade #$unidade.id inativada. Não pode ser removida, pois já possui Funcionários/Máquinas"
            }
        }
        log.info ret.message
        ret
    }
}
