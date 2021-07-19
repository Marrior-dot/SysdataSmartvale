package com.sysdata.gestaofrota

import grails.converters.JSON
import org.springframework.http.HttpStatus

//@Secured(['IS_AUTHENTICATED_FULLY'])
class MaquinaMotorizadaController {

    MaquinaMotorizadaService maquinaMotorizadaService


    def listFuncionariosJSON() {
        MaquinaMotorizada maquinaInstance
        if(params?.instance == "Veiculo"){
             maquinaInstance = Veiculo.get(params.long('id'))
        }else{
             maquinaInstance = Equipamento.get(params.long('id'))
        }

        def funcionariosList
        def data
        if(maquinaInstance?.funcionarios!= null){
            funcionariosList = maquinaInstance?.funcionarios?.collect { f ->
                [
                        id       : f.funcionario.id,
                        matricula: f.funcionario.matricula,
                        nome     : f.funcionario.nome,
                        cpf      : f.funcionario.cpf,
                        acao     : "<a href='#' onclick='removeFuncionario(${f.funcionario.id});'>" +
                                "<span class='glyphicon glyphicon-trash'/></a>"
                ]
            }
            data = [totalRecords: funcionariosList.size()?:0, results: funcionariosList?.sort { it.nome }]
        }else{
            data = [totalRecords: 0, results: []]
        }
       render data as JSON
    }

    def addFuncionario() {
        MaquinaMotorizada maquina = MaquinaMotorizada.get(params?.long('id'))
        if (! maquina) {
            render status: HttpStatus.NOT_FOUND, text: "Equipamento/Veiculo não encontrado"
            return
        }
        def ret = maquinaMotorizadaService.linkMaquinaToFuncionario(maquina, params)
        if (ret.success) {
            render status: HttpStatus.OK, text: "Funcionário(s) relacionado(s) ao ${params?.instanceName} com sucesso!"
            return
        } else {
            render status: HttpStatus.INTERNAL_SERVER_ERROR, text: ret.message
            return
        }
    }

    def removeFuncionario() {
        MaquinaMotorizada maquina = MaquinaMotorizada.get(params.long('id'))
        if (! maquina) {
            render status: HttpStatus.NOT_FOUND, text: "Equipamento/Veiculo não encontrado"
            return
        }
        long funcId = params.long('funcionario')
        if (! funcId) {
            render status: HttpStatus.NOT_FOUND, text: "Id de funcionário não informado."
            return
        }
        def ret = maquinaMotorizadaService.unlinkMaquinaToFuncionario(maquina, funcId)
        if (ret.success) {
            render status: HttpStatus.OK, text: ret.message
            return
        } else {
            render status: HttpStatus.INTERNAL_SERVER_ERROR, text: ret.message
            return
        }

    }
}
