package com.sysdata.gestaofrota.relatorios
import com.sysdata.gestaofrota.Estabelecimento

import grails.core.GrailsApplication

class BaseEstabelecimentosController {

    def exportService

    GrailsApplication grailsApplication

    BaseEstabelecimentosService  baseEstabelecimentosService


    def index() {


        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0


        if (params?.f && params.f != "html") {
            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=baseEstabelecimentos.${params.extension}")



            List fields = ["empresa.cnpj", "codigo", "unidade.rh.nomeFantasia","nome", "cpf",]

            Map labels = ["matricula": "Matrícula", "nome": "Nome", "cpf": "CPF", "unidade.rh.nomeFantasia": "Empresa Cliente"]


            //  Map formatters = [author: upperCase]
            Map parameters = [title: "Base de Estabelecimentos", "column.widths": [0.2, 0.3, 0.5]]

            exportService.export(params.f,
                                response.outputStream,
                                baseEstabelecimentosService.list(params, false),
                                fields,
                                labels, [:], [:])

            return

        }

        [baseEstabelecimentosList: baseEstabelecimentosService.list(params), baseEstabelecimentosCount: baseEstabelecimentosService.count(params)]
    }


    }

