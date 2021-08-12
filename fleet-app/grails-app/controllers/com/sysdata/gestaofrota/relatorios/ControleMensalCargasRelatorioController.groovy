package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Unidade
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

            def cabecalhoMensalCargasRelatorio = []
            def cabecalho = [:]
            cabecalho.cliente = "EMISSAO"
            cabecalho.unidade = new Date().format('dd/MM/yyyy')
            cabecalhoMensalCargasRelatorio << cabecalho

            def cabecalho1 = [:]
            if (params.empresa) {
                cabecalho1.cliente = "EMPRESA"
                Rh empresaCliente = Rh.get(params.empresa.toLong())
                cabecalho1.unidade = empresaCliente.nomeFantasia
                cabecalhoMensalCargasRelatorio << cabecalho1
            }
            def cabecalho2 = [:]
            if (params.unidade) {
                cabecalho2.cliente = "UNIDADE"
                Unidade unidade = Unidade.get(params.unidade.toLong())
                cabecalho2.unidade = unidade.nome
                cabecalhoMensalCargasRelatorio << cabecalho2
            }
            def cabecalho3 = [:]
            if (params.dataInicio) {
                cabecalho3.cliente = "DT. Inicio"
                cabecalho3.unidade = params.dataInicio
                cabecalhoMensalCargasRelatorio << cabecalho3
            }
            def cabecalho4 = [:]
            if (params.dataFim) {
                cabecalho4.cliente = "DT. Fim"
                cabecalho4.unidade = params.dataFim
                cabecalhoMensalCargasRelatorio << cabecalho4
            }
            def cabecalho5 = [:]
            cabecalho5.cliente = ""
            cabecalhoMensalCargasRelatorio << cabecalho5

            def cabecalho6 = [:]
            cabecalho6.cliente = "CLIENTE"
            cabecalho6.unidade = "UNIDADE"
            cabecalho6.pedidoId = "COD. PEDIDO"
            cabecalho6.pedidoDataCriacao = "DT PEDIDO"
            cabecalho6.pedidoDataCarga = "DT CARGA"
            cabecalho6.identificadorMaquina = "PLACA/COD. EQUIP"
            cabecalho6.valor = "VALOR CARGA"
            cabecalho6.pedidoValidade = "VALIDADE"
            cabecalhoMensalCargasRelatorio << cabecalho6

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
            //Pedir ajuda ao Luiz como totalizar as cargas no Excel fizemos o collection no Service, por isso o problema.
            /*def report = controleMensalCargasService.list(params, false)
            def totalValorC = report.sum { it.valor }
            report +=
                    [
                            cliente: "",
                            unidade: "",
                            pedidoId:"",
                            pedidoDataCriacao: "",
                            pedidoDataCarga: "",
                            identificadorMaquina: "TOTAL CARGA",
                            valor: Util.formatCurrency(totalValorC),
                            pedidoValidade: ""
                    ]*/

            List fields =             [
                    "cliente",
                    "unidade",
                    'pedidoId',
                    'pedidoDataCriacao',
                    'pedidoDataCarga',
                    'identificadorMaquina',
                    'valor',
                    /*'pedidoTotal',
                    'pedidoTaxa',
                    'pedidoTaxaDesconto',*/
                    'pedidoValidade'
            ]

            Map labels = ["cliente": "Cliente",
                          "unidade": "Unidade",
                          "pedidoId": "Cod Pedido",
                          "pedidoDataCriacao": "Data Pedido",
                          "pedidoDataCarga": "Data Carga",
                          'identificadorMaquina': "Placa/Cod.Equip.",
                          "valor": "Valor Carga",
                          /*"pedidoTotal": "Valor total Pedido",
                          "pedidoTaxa": "Taxa(%),",
                          "pedidoTaxaDesconto": "Taxa Desconto(%)",*/
                          "pedidoValidade": "Validade(dias)"]


            //  Map formatters = [author: upperCase]
            Map parameters = [title: "Controle Mensal de Cargas", "column.widths": [0.2, 0.3, 0.5]]

            exportService.export(params.f,
                                response.outputStream,
                    cabecalhoMensalCargasRelatorio+controleMensalCargasService.list(params, false),

                                fields,
                                labels, [:], ['header.enabled': false])
            return
        }

        [controleMensalCargasList: controleMensalCargasService.list(params), controleMensalCargasCount: controleMensalCargasService.count(params)]
    }


    }

