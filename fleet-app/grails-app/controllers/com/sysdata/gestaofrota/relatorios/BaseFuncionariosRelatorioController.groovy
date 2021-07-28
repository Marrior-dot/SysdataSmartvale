package com.sysdata.gestaofrota.relatorios

import grails.core.GrailsApplication

class BaseFuncionariosRelatorioController {

    def exportService

    GrailsApplication grailsApplication


    BaseFuncionariosService baseFuncionariosService


    def index() {


        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0


        if (params?.f && params.f != "html") {
            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=baseFuncionarios.${params.extension}")


            List fields = [
                            "unidade.rh.nomeFantasia",
                           "unidade.nome",
                           "matricula",
                           "nome",
                           "cpf",
                           "portador.cartaoAtual.numero",
                           "cnh",
                           "categoriaCnh.nome",
                           "validadeCnh",
                           "email",
                           "telefone",
                           "endereco.logradouro",
                           "endereco.numero",
                           "endereco.complemento",
                           "endereco.bairro",
                           "endereco.cidade.nome",
                           "endereco.cidade.estado",
                           "endereco.cep"]

            Map labels = ["unidade.rh.nomeFantasia": "Cliente",
                          "unidade.nome": "Unidade",
                          "matricula": "Matrícula",
                          "nome": "Nome",
                          "cpf": "CPF",
                          "portador.cartaoAtual.numero": "Cartão",
                          "cnh": "CNH",
                          "categoriaCnh.nome": "CNH Categoria",
                          "validadeCnh": "CNH Validade",
                          "email": "Email",
                          "telefone": "Telefone",
                          "endereco.logradouro": "Logradouro",
                          "endereco.numero": "Numero",
                          "endereco.complemento": "Complemento",
                          "endereco.bairro": "Bairro",
                          "endereco.cidade.nome": "Cidade",
                          "endereco.cidade.estado": "Estado",
                          "endereco.cep": "Cep" ]


            //  Map formatters = [author: upperCase]
            Map parameters = [title: "Base de Funcionarios", "column.widths": [0.2, 0.3, 0.5]]

            exportService.export(params.f,
                                response.outputStream,
                                baseFuncionariosService.list(params, false),
                                fields,
                                labels, [:], [:])
            return 
        }

        [baseFuncionariosList: baseFuncionariosService.list(params), baseFuncionariosCount: baseFuncionariosService.count(params)]
    }


    }

