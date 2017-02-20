package com.sysdata.gestaofrota

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.springframework.http.HttpStatus

@Secured(['IS_AUTHENTICATED_FULLY'])
class MaquinaMotorizadaController {

    def listFuncionariosJSON() {
        MaquinaMotorizada maquinaInstance = MaquinaMotorizada.get(params.long('id'))
        def funcionariosList = maquinaInstance.funcionarios.collect { f ->
            [
                    id       : f.funcionario.id,
                    matricula: f.funcionario.matricula,
                    nome     : f.funcionario.nome,
                    cpf      : f.funcionario.cpf,
                    acao     : "<a href='#' onclick='removeFuncionario(${f.funcionario.id});'>" +
                            "<img src='${resource(dir: 'images', file: 'remove_person.png')}' alt='Remover'>" +
                            "</a>"
            ]
        }
        def data = [totalRecords: funcionariosList.size(), results: funcionariosList.sort { it.nome }]
        render data as JSON
    }

    def addFuncionario = {
        MaquinaMotorizada instance = MaquinaMotorizada.get(params?.long('id'))
        if (!instance) {
            render status: HttpStatus.NOT_FOUND, text: "Equipamento/Veiculo não encontrado"
            return;
        }

        List<Long> idsFuncionariosSelecionados = params.list('funcionarios[]').collect { it as Long }

        for (int i = 0; i < idsFuncionariosSelecionados.size(); i++) {
            long id = idsFuncionariosSelecionados[i]
            MaquinaFuncionario maqFuncInstance = instance.funcionarios.find { it.funcionario.id == id }
            if (maqFuncInstance) continue; //funcionario já associado a instancia! Continuar para o próximo da lista

            Funcionario funcionarioInstance = Funcionario.get(id)

            if (funcionarioInstance) {
                MaquinaFuncionario instanceFuncionario = new MaquinaFuncionario()
                instanceFuncionario.funcionario = funcionarioInstance
                instanceFuncionario.status = Status.ATIVO
                instance.addToFuncionarios(instanceFuncionario)
            }
        }

        instance.save()
        render status: HttpStatus.OK, text: "Funcionário(s) relacionado(s) ao ${params?.instanceName} com sucesso!"
    }

    def removeFuncionario = {
        MaquinaMotorizada instance = MaquinaMotorizada.get(params.long('id'))
        if (!instance) {
            render status: HttpStatus.NOT_FOUND, text: "Equipamento/Veiculo não encontrado"
            return;
        }

        long funcionarioId = params.long('funcionario')
        if (!funcionarioId) {
            render status: HttpStatus.NOT_FOUND, text: "Id de funcionário não cedido."
            return;
        }

        MaquinaFuncionario maqFuncInstance = instance.funcionarios.find { it.funcionario.id == funcionarioId }
        if (maqFuncInstance) {
            instance.removeFromFuncionarios(maqFuncInstance)
            maqFuncInstance.delete()
            if (MaquinaFuncionario.exists(maqFuncInstance.id))
                render "Relação entre ${params?.instanceName} e Funcionários removida."
            else {
                //TODO: verificar se é necessário a linha abaixo
                //maqFuncInstance.status = Status.INATIVO
                render "Relação entre ${params?.instanceName} e Funcionários não pode ser removida, apenas desativada."
            }
        }
    }
}
