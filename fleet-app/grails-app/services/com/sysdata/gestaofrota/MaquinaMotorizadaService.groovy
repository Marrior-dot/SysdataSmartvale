package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class MaquinaMotorizadaService {

    def linkMaquinaToFuncionario(MaquinaMotorizada maquina, Map params) {
        def ret = [success: true]

        List<Long> idsFuncionariosSelecionados = params.list('funcionarios[]').collect { it as Long }

        for (int i = 0; i < idsFuncionariosSelecionados.size(); i++) {
            long id = idsFuncionariosSelecionados[i]
            MaquinaFuncionario maqFuncInstance = maquina.funcionarios.find { it.funcionario.id == id }
            if (maqFuncInstance) {
                log.warn "Func #${id} ja vinculado a Maquina #${maquina.id}"
                ret.success = false
                ret.message = "Funcionário já vinculado à Máquina!"
                break //funcionario já associado a instancia! Continuar para o próximo da lista
            }

            Funcionario funcionarioInstance = Funcionario.get(id)

            if (funcionarioInstance) {
                MaquinaFuncionario instanceFuncionario = new MaquinaFuncionario()
                instanceFuncionario.funcionario = funcionarioInstance
                instanceFuncionario.status = Status.ATIVO
                maquina.addToFuncionarios(instanceFuncionario)
            }
        }
        if (!ret.success)
            return ret

        if (! maquina.save(flush: true)) {
            ret.success = false
            ret.message = maquina.errors
            return ret
        }
        ret
    }

    def unlinkMaquinaToFuncionario(MaquinaMotorizada maquina, long funcId) {
        def ret = [success: true]
        MaquinaFuncionario maqFuncInstance = maquina.funcionarios.find { it.funcionario.id == funcId }
        if (maqFuncInstance) {
            maquina.removeFromFuncionarios(maqFuncInstance)
            maqFuncInstance.delete(flush: true)
            //maquina.save(flush: true)
            ret.message = "Relação entre Funcionário e Máquina removida com sucesso"
            log.info "Relação entre Funcionário #$funcId e Máquina #$maquina.id removida"
            return ret

/*
            if (MaquinaFuncionario.exists(maqFuncInstance.id)) {
                ret.message = "Relação entre Funcionário e Máquina removida."
                log.info "Relação entre Funcion"
                return ret
            } else {
                //TODO: verificar se é necessário a linha abaixo
                //maqFuncInstance.status = Status.INATIVO
                render "Relação entre ${params?.instanceName} e Funcionários não pode ser removida, apenas desativada."
            }
*/
        } else {

            ret.success = false
            ret.message = "Erro Interno. Contatar suporte."
            log.error "Relação entre Funcionário #$funcId e Máquina #$maquina.id não existe"
            return ret
        }
    }

}
