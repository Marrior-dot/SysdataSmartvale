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
        def ret = [success: true]

        // Controle de Limite para Cliente Pré-Pago
        if (rh.modeloCobranca == TipoCobranca.PRE_PAGO ) {
            if (!rh.id)
                rh.saldoDisponivel = rh.limiteTotal
            else {
                def delta = rh.limiteTotal ?: 0 - rh.getPersistentValue('limiteTotal') ?: 0
                if (delta > 0) {
                    rh.saldoDisponivel = rh.saldoDisponivel ?: 0 + delta
                } else if (delta < 0) {
                    def novoSaldo = rh.saldoDisponivel + delta
                    rh.saldoDisponivel = novoSaldo > 0 ? novoSaldo : 0

                }
            }
        }


        // Verifica se novo valor do limite interfere nos limites de cartões vinculados cadastrados
        if (rh.modeloCobranca == TipoCobranca.POS_PAGO && rh.limiteTotal < rh.limiteComprometido) {
            ret.success = false
            ret.message = "Limite comprometido com cartões vinculados não pode ser superior ao limite total informado para a Empresa"
        }

        if (! rh.save(flush: true))
            ret.success = false

        return ret
    }

    def update(Rh rh) {
        def ret = save(rh)

        def hasFuncionarios = Funcionario.countFuncionariosRh(rh).get() > 0
        def cannotUpdate = rh.isDirty("vinculoCartao") && rh.vinculoCartao != rh.getPersistentValue("vinculoCartao") && hasFuncionarios
        if (! cannotUpdate) {
            cannotUpdate = rh.isDirty("cartaoComChip") && rh.cartaoComChip != rh.getPersistentValue("cartaoComChip") && hasFuncionarios
            if (! cannotUpdate) {

                if (! rh.save(flush: true)) {
                    ret.success = false
                    return ret
                }

                log.info "EMP #$rh.id atualizada"
            } else {
                ret.success = false
                ret.message = "Tipo de Cartão (Com ou Sem Chip) não pode ser mais alterado. Empresa já possui funcionários cadastrados"
            }
        } else {
            ret.success = false
            ret.message = "Vínculo de Cartão a Funcionário ou Veículo/Máquina não pode ser mais alterado. Empresa já possui funcionários cadastrados"
        }

        return ret

    }

}
