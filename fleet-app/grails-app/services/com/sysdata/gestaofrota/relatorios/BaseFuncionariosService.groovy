package com.sysdata.gestaofrota.relatorios
import com.sysdata.gestaofrota.Funcionario
import grails.core.GrailsApplication



class BaseFuncionariosService {


    def list(params) {

        def baseFuncionariosList = Funcionario.executeQuery("""
        select
            f.matricula,
            f.nome,
            f.cpf,
            f.unidade.rh.nome,
            f.unidade.nome,
            f.categoriaCnh,
            f.validadeCnh,
            f.veiculos.size


            from Funcionario f



        """, [
        ], [max: params.max ? params.max as int : 10, offset: params.offset ? params.offset as int : 0])

        return baseFuncionariosList
    }

    def count() {
        def baseFuncionariosCount = Funcionario.executeQuery("""
        select
            count(f.id)
        from

            Funcionario f,



        """, [

        ])

        return baseFuncionariosCount

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
