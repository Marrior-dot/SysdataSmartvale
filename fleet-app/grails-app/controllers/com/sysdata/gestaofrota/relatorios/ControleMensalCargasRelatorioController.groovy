package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Util


class ControleMensalCargasRelatorioController {

    def exportService

    ControleMensalCargasService controleMensalCargasService


    def index() {


        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0


        if (params?.f && params.f != "html") {
            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=controleMensalCargas.${params.extension}")


/*
            [
                    cliente: it.pedido.unidade.rh.nomeFantasia,
                    unidade: it.pedido.unidade.nome,
                    pedidoId: it.pedido.id,
                    pedidoDataCriacao: it.pedido.dateCreated,
                    pedidoDataCarga: it.pedido.dataCarga,
                    identificadorMaquina: it.maquina.identificacaoCompacta,
                    valor: it.valor,
                    pedidoTotal: it.pedido.itens.sum { it.valor },
                    pedidoTaxa: it.pedido.taxa,
                    pedidoTaxaDesconto: it.pedido.taxaDesconto,
                    pedidoValidade: it.pedido.validade
            ]
*/


            List fields =             [
                    "cliente",
                    "unidade",
                    'pedidoId',
                    'pedidoDataCriacao',
                    'pedidoDataCarga',
                    'identificadorMaquina',
                    'valor',
                    'pedidoTotal',
                    'pedidoTaxa',
                    'pedidoTaxaDesconto',
                    'pedidoValidade'
            ]

            Map labels = ["cliente": "Cliente",
                          "unidade": "Unidade",
                          "pedidoId": "Cod Pedido",
                          "pedidoDataCriacao": "Data Pedido",
                          "pedidoDataCarga": "Data Carga",
                          'identificadorMaquina': "Placa/Cod.Equip.",
                          "valor": "Valor Carga",
                          "pedidoTotal": "Valor total Pedido",
                          "pedidoTaxa": "Taxa(%),",
                          "pedidoTaxaDesconto": "Taxa Desconto(%)",
                          "pedidoValidade": "Validade(dias)"]


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

