package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

import java.lang.reflect.Array

@Transactional
class RhService {

    UnidadeService unidadeService

    private void deleteAllUsers(Rh cliente) {
        def usersIds = User.withCriteria {
            projections {
                property "id"
            }
            eq("owner", cliente)
        }
        if (usersIds) {
            usersIds.each { uid ->
                User user = User.get(uid)
                UserRole.removeAll(user)
                log.info "(-) USR #${uid}"
                user.delete()
            }
        }
    }

    def delete(Rh rh) {
        log.info "Excluindo Cliente #${rh.id}..."
        def msg = ""
        if (rh.unidades.isEmpty()) {
            rh.empresas.clear()
            deleteAllUsers(rh)
            rh.delete(flush: true)
            msg = "(-) Cli $rh "
        } else {
            def canDelete = true
            def unidIds = rh.unidades*.id
            rh.empresas.clear()
            deleteAllUsers(rh)
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

    def save(Rh rh, Map params) {
        def ret = [success: true]

        if (! rh.id && rh.modeloCobranca == TipoCobranca.PRE_PAGO )
            rh.saldoDisponivel = rh.limiteTotal

        // Verifica se novo valor do limite interfere nos limites de cartões vinculados cadastrados
        if (rh.modeloCobranca == TipoCobranca.POS_PAGO && rh.limiteTotal < rh.limiteComprometido) {
            ret.success = false
            ret.message = "Limite comprometido com cartões vinculados não pode ser superior ao limite total informado para a Empresa"
        }

        if (! rh.save(flush: true))
            ret.success = false

        return ret
    }

    def update(Rh rh, Map params) {

        rh.properties = params

        // Controle de Limite para Cliente Pré-Pago
        if (rh.modeloCobranca == TipoCobranca.PRE_PAGO ) {

            def oldLimite = rh.getPersistentValue("limiteTotal") ?: 0
            def newLimite = rh.limiteTotal
            def currSaldo = rh.saldoDisponivel ?: 0

            def delta = newLimite - oldLimite
            def newSaldo = currSaldo + delta
            rh.saldoDisponivel = newSaldo > 0 ? newSaldo : 0
        }

        def ret = save(rh, params)
        if (! ret.success)
            return ret

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

    /**
     *  Recupera todos os ECs vinculados ao RH que satisfazem os critérios
     */

    Map listEstabsVinculados(Rh rh, params) {

        def criteria = {

            programas {
                eq("id", rh.id)
            }

            if (params.fantasia)
                ilike("nomeFantasia", params.fantasia + '%')

            if (params['cids[]']) {
                def cids = params['cids[]'].collect { it as long }

                endereco {
                    cidade {
                        'in'("id", cids)
                    }
                }
            } else {
                if (params['ufs[]']) {
                    def ufs = params['ufs[]'].collect { it as long }

                    endereco {
                        cidade {
                            estado {
                                'in'("id", ufs)
                            }
                        }
                    }
                }
            }
        }

        params.sort = "nomeFantasia"
        def estabList = PostoCombustivel.createCriteria().list(params, criteria)
        def estabCount = PostoCombustivel.createCriteria().count(criteria)

        return [estabList: estabList, estabCount: estabCount]
    }

    /**
     *  Recupera todos os ECs na base que satisfazem os critérios
     */

    Map editEstabsVinculados(params) {

        def criteria = {
            if (params.fantasia)

                ilike("nomeFantasia", params.fantasia + '%')

            if (params['cids[]']) {

                def cids = params['cids[]'] instanceof String[] ? params['cids[]'].collect { it as long } : params['cids[]'] as long

                endereco {
                    cidade {
                        'in'("id", cids)
                    }
                }
            } else {
                if (params['ufs[]']) {

                    def ufs = params['ufs[]'] instanceof String[] ? params['ufs[]'].collect { it as long } : params['ufs[]'].toLong()

                    endereco {
                        cidade {
                            estado {
                                'in'("id", ufs)
                            }
                        }
                    }
                }
            }
        }

        params.sort = "nomeFantasia"
        def estabList = PostoCombustivel.createCriteria().list(params, criteria)
        def estabCount = PostoCombustivel.createCriteria().count(criteria)

        return [estabList: estabList, estabCount: estabCount]
    }

    def saveEstabsVinculados(Rh rh, params) {

        def estList = rh.empresas.asList().clone()

        params.ecs.each { est ->

            PostoCombustivel estab = estList.find { it.id == est.id as long }
            if (estab && !est.checked) {
                rh.removeFromEmpresas(estab)
                rh.save(flush: true)
                log.info "\t(-) EC #$estab.id a RH #$rh.id"
            } else if (!estab && est.checked){
                estab = PostoCombustivel.get(est.id)
                rh.addToEmpresas(estab)
                rh.save(flush: true)
                log.info "\t(+) EC #$estab.id a RH #$rh.id"
            }

        }
    }

}
