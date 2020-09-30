package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Funcionario

import grails.core.GrailsApplication

class BaseFuncionariosController {

    def exportService

    GrailsApplication grailsApplication

    BaseFuncionariosService  baseFuncionariosService


    def index() {


        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0


        if (params?.f && params.f != "html") {
            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=baseFuncionarios.${params.extension}")



            List fields = ["matricula", "nome", "cpf","portador.cartoes.numero", "unidade.rh.nomeFantasia", "unidade.nome",
                           "cnh", "categoriaCnh.nome", "validadeCnh", "email", "telefone", "portador.endereco.logradouro",
                           "portador.endereco.cidade"]

            Map labels = ["matricula": "Matrícula", "nome": "Nome", "cpf": "CPF","portador.cartoes.numero": "Cartão",
                          "unidade.rh.nomeFantasia": "Empresa Cliente", "unidade.nome": "Unidade", "cnh": "CNH",
                          "categoriaCnh.nome": "CNH Categoria", "validadeCnh": "CNH Validade", "email": "Email", "telefone": "Telefone,",
                          "portador.endereco.logradouro": "Logradouro","portador.endereco.cidade": "Cidade-UF"]


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

