package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Funcionario

import grails.core.GrailsApplication

class BaseFuncionariosController {

    def exportService

    GrailsApplication grailsApplication

    BaseFuncionariosService  baseFuncionariosService


    def index() {




        if (params.f && params.f != 'html') {



            def list = {
                if (!params.max) params.max = 10

                if (params?.format && params.format != "html") {
                    response.contentType = grailsApplication.config.grails.mime.types[params.format]
                    response.setHeader("Content-disposition", "attachment; filename=baseFuncionarios.${params.extension}")


                    List fields = ["matricula", "nome", "cpf", "rh", "unidade"]
                    Map labels = ["Matricula": "matricula", "Nome": "nome", "Nome": "nome", "CPF": "nome", "Empresa": "rh", "Unidade": "unidade"]

                    /* Formatter closure in previous releases
                def upperCase = { value ->
                    return value.toUpperCase()
                }


                // Formatter closure
                def upperCase = { domain, value ->
                    return value.toUpperCase()
                }*/

                    //  Map formatters = [author: upperCase]
                    Map parameters = [title: "Base de Funcionarios", "column.widths": [0.2, 0.3, 0.5]]

                    exportService.export(params.format, response.outputStream, baseFuncionariosList(params), fields, labels, formatters, parameters)


                }




            }


        }

        [baseFuncionariosList: baseFuncionariosService.list(params), baseFuncionariosCount: baseFuncionariosService.count()]
    }


    }

