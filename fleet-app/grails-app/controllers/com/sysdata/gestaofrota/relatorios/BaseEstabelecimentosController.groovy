package com.sysdata.gestaofrota.relatorios
import com.sysdata.gestaofrota.Estabelecimento
import com.sysdata.gestaofrota.Empresa

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



            List fields = ["codigo", "cnpj", "nome","nomeFantasia", "endereco.logradouro","endereco.cidade","telefone","email","empresa.taxaReembolso"]

            Map labels = ["codigo": "Estab.Codigo", "cnpj": "CNPJ", "nome": "Razão Social", "nomeFantasia": "Fantasia",
                          "endereco.logradouro": "Logradouro","endereco.cidade": "Cidade-UF",
                          "telefone": "Telefone","email": "Email", "empresa.taxaReembolso": "Taxa Reembolso"]


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

