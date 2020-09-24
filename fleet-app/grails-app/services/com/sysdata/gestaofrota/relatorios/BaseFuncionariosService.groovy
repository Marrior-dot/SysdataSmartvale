package com.sysdata.gestaofrota.relatorios
import com.sysdata.gestaofrota.Funcionario
import grails.core.GrailsApplication



class BaseFuncionariosService {


    def list(params, paginate = true) {

        def criteria = {

            if (params.empresa)
                unidade {
                    rh {
                        eq("id", params.empresa.toLong())
                    }
                }
            if (params.unidade) {

                unidade {
                    eq("id", params.unidade.toLong())
                }
            }

        }


        if (paginate)
            return Funcionario.createCriteria().list([max: params.max, offset: params.offset], criteria)
        else
            return Funcionario.createCriteria().list(criteria)

    }

    def count(pars) {

        def criteria = {

            if (pars.empresa)
                unidade {
                    rh {
                        eq("id", pars.empresa.toLong())
                    }
                }
            if (pars.unidade) {

                unidade {
                    eq("id", pars.unidade.toLong())
                }
            }

        }

        return Funcionario.createCriteria().count(criteria)

    }

/*
    def list = {
        if(!params.max) params.max = 10

        if(params?.format && params.format != "html"){
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=baseFuncionarios.${params.extension}")


            List fields = ["matricula", "nome", "cpf", "rh", "unidade"]
            Map labels = ["Matricula": "matricula", "Nome": "nome", "Nome": "nome", "CPF": "nome","Empresa": "rh","Unidade": "unidade"]

            /* Formatter closure in previous releases
			def upperCase = { value ->
				return value.toUpperCase()
			}


            // Formatter closure
            def upperCase = { domain, value ->
                return value.toUpperCase()
            }

          //  Map formatters = [author: upperCase]
            Map parameters = [title: "Base de Funcionarios", "column.widths": [0.2, 0.3, 0.5]]

            exportService.export(params.format, response.outputStream, baseFuncionariosList.list(params), fields, labels, formatters, parameters)
        }

        [ baseFuncionariosList: baseFuncionariosList.list( params ) ]
    }

*/




    }
