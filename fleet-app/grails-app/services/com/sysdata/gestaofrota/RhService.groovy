package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class RhService {

    UnidadeService unidadeService

    def delete(Rh rh) {
        def msg = ""
        if (rh.unidades.isEmpty()) {
            rh.delete(flush: true)
            msg = "Empresa $rh removida"
        } else {
            def canDelete = true
            def unidIds = rh.unidades*.id
            unidIds.each { uid ->
                def ret = unidadeService.delete(Unidade.get(uid))
                if (! ret.success) {
                    if (msg)
                        msg += "\n"
                    msg += ret.message
                    canDelete &= false
                }
            }
            if (canDelete) {
                rh.delete(flush: true)
                msg += "Empresa $rh removida"
            } else {
                rh.status = Status.INATIVO
                rh.save(flush: true)
                msg += "Empresa #$rh.id inativada. Já tem Unidades vinculadas"
            }
        }
        log.info msg
        msg
    }

    def save(Rh rh) {
        def ret = [:]
        ret.success = true
        ret.message = ""
        def hasFuncionarios = Funcionario.countFuncionariosRh(rh).get() > 0
        def cannotUpdate = rh.isDirty("vinculoCartao") && rh.vinculoCartao != rh.getPersistentValue("vinculoCartao") && hasFuncionarios
        if (! cannotUpdate) {
            cannotUpdate = rh.isDirty("cartaoComChip") && rh.cartaoComChip != rh.getPersistentValue("cartaoComChip") && hasFuncionarios
            if (! cannotUpdate) {
                rh.save(flush: true)
                log.info "EMP #$rh.id atualizada"
            } else {
                ret.success = false
                ret.message = "Tipo de Cartão (Com ou Sem Chip) não pode ser mais alterado. Empresa já possui funcionários cadastrados"
            }
        } else {
            ret.success = false
            ret.message = "Vínculo de Cartão a Funcionário ou Veículo/Máquina não pode ser mais alterado. Empresa já possui funcionários cadastrados"
        }

        // Verifica se novo valor do limite interfere nos limites de cartões vinculados cadastrados

        if (rh.limiteTotal < rh.limiteComprometido.list()[0]) {
            ret.success = false
            ret.message = "Limite comprometido com cartões vinculados não pode ser superior ao limite total informado para a Empresa"
        }
        return ret
    }

}
