package com.sysdata.gestaofrota.relatorios


import grails.core.GrailsApplication
import com.sysdata.gestaofrota.Funcionario
import com.sysdata.gestaofrota.ItemPedidoParticipante


class ControleMensalCargasRelatorioController {

    def exportService

    GrailsApplication grailsApplication


    ControleMensalCargasService controleMensalCargasService


    def index() {


        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0


        if (params?.f && params.f != "html") {
            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=controleMensalCargas.${params.extension}")


            List fields = ["pedido.unidade.rh.nomeFantasia", "pedido.unidade.nome","pedido.id","pedido.dateCreated",
                           "pedido.dataCarga", "pedido.status","participante.matricula","participante.nome",
                           "valor", "pedido.itens.valor.sum()", "pedido.taxa", "pedido.taxaDesconto", "pedido.validade",
                          ]

            Map labels = ["pedido.unidade.rh.nomeFantasia": "Cliente", "pedido.unidade.nome": "Unidade","pedido.id": "Cod Pedido",
                          "pedido.dateCreated": "Data Pedido", "pedido.dataCarga": "Data Carga","pedido.status": "Status",
                          "participante.matricula": "Matricula","participante.nome": "Funcionario", "valor": "Valor Carga",
                          "pedido.itens.valor.sum()": "Valor total Pedido", "pedido.taxa": "Taxa(%),",
                          "pedido.taxaDesconto": "Taxa Desconto(%)","pedido.validade": "Validade(dias)"]


            //  Map formatters = [author: upperCase]
            Map parameters = [title: "Controle Mensal de Cargas", "column.widths": [0.2, 0.3, 0.5]]

            exportService.export(params.f,
                                response.outputStream,
                                controleMensalCargasService.list(params, false),

                                fields,
                                labels, [:], [:])
            return
        }

        [controleMensalCargasList: controleMensalCargasService.list(params), controleMensalCargasCount: controleMensalCargasService.count(params)]
    }


    }

