package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Equipamento
import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Unidade
import com.sysdata.gestaofrota.Util
import com.sysdata.gestaofrota.Veiculo

class HistoricoFrotaRelatorioController {

    def exportService
    HistoricoFrotaService historicoFrotaService

    def index() {
        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0

        if (params?.f && params.f != "html") {
            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=historicoFrota.${params.extension}")

            def cabecalhoHistoricoFrotaRelatorio = []
            def cabecalho = [:]
            cabecalho.nsu = "EMISSAO"
            cabecalho.dataHora = new Date().format('dd/MM/yyyy')
            cabecalhoHistoricoFrotaRelatorio << cabecalho

            def cabecalho1 = [:]
            if (params.empresa) {
                cabecalho1.nsu = "EMPRESA"
                Rh empresaCliente = Rh.get(params.empresa.toLong())
                cabecalho1.dataHora = empresaCliente.nomeFantasia
                cabecalhoHistoricoFrotaRelatorio << cabecalho1
            }
            def cabecalho2 = [:]
            if (params.unidade) {
                cabecalho2.nsu = "UNIDADE"
                Unidade unidade = Unidade.get(params.unidade.toLong())
                cabecalho2.dataHora = unidade.nome
                cabecalhoHistoricoFrotaRelatorio << cabecalho2
            }
            def cabecalho3 = [:]
            if (params.placa) {
                cabecalho3.nsu = "PLACA"
                cabecalho3.dataHora = params.placa
                cabecalhoHistoricoFrotaRelatorio << cabecalho3
            }
            def cabecalho4 = [:]
            if (params.codigo) {
                cabecalho4.nsu = "PLACA"
                cabecalho4.dataHora = params.placa
                cabecalhoHistoricoFrotaRelatorio << cabecalho4
            }
            def cabecalho5 = [:]
            if (params.dataInicio) {
                cabecalho5.nsu = "DT. Inicio"
                cabecalho5.dataHora = params.dataInicio
                cabecalhoHistoricoFrotaRelatorio << cabecalho5
            }
            def cabecalho6 = [:]
            if (params.dataFim) {
                cabecalho6.nsu = "DT. Fim"
                cabecalho6.dataHora = params.dataFim
                cabecalhoHistoricoFrotaRelatorio << cabecalho6
            }

            def cabecalho7 = [:]
            cabecalho7.nsu = ""
            cabecalhoHistoricoFrotaRelatorio << cabecalho7

            def cabecalho8 = [:]
            cabecalho8.nsu = "NSU"
            cabecalho8.dataHora = "DATA/HORA"
            cabecalho8.terminal = "TERMINAL"
            cabecalho8.estabelecimento = "ESTABELECIMENTO"
            cabecalho8.veiculo = "VEICULO"
            cabecalho8.km = "HODOMETRO"
            cabecalho8.equipamento = "EQUIPAMENTO"
            cabecalho8.funcionario = "FUNCIONARIO"
            cabecalho8.produtos = "VALOR TOTAL"
            cabecalho8.precoUnitario = "PRODUTOS"
            cabecalho8.valor = "QTD LITROS"
            cabecalho8.litros = "PREÇO UNITARIO"
            cabecalho8.cliente = "CLIENTE"
            cabecalho8.unidade = "UNIDADE"
            cabecalho8.tipoTransacao = "TIPO TRANSACAO"
            cabecalho8.statusTransacao = "STATUS"
            cabecalhoHistoricoFrotaRelatorio << cabecalho8

            def reportList = historicoFrotaService.list(params, false)

            def totalValor = reportList.sum { it.valor }

            reportList = reportList.collect { tr ->
                                [
                                    "nsu": tr.nsu,
                                    "dataHora": tr.dateCreated.format('dd/MM/yy HH:mm:ss'),
                                    "terminal": tr.terminal,
                                    "estabelecimento": tr.estabelecimento.identificacaoResumida,
                                    "veiculo": tr.maquina.instanceOf(Veiculo) ? (tr.maquina as Veiculo).identificacaoCompacta : '',
                                    "km": tr.quilometragem,
                                    "equipamento": tr.maquina.instanceOf(Equipamento) ? (tr.maquina as Equipamento).identificacaoCompacta : '',
                                    "funcionario": "(${tr.participante.matricula}) ${tr.participante.nome}",
                                    "produtos": "${tr.produtos*.produto.nome.join('')}",
                                    "precoUnitario": Util.formatCurrency(tr.precoUnitario),
                                    "valor": Util.formatCurrency(tr.valor),
                                    "litros": tr.qtd_litros,
                                    "cliente": tr.cartao.portador.unidade.rh.nome,
                                    "unidade": tr.cartao.portador.unidade.nome,
                                    "tipoTransacao": tr.tipo.nome,
                                    "statusTransacao": tr.statusControle.nome,

                                ]
                            }

            reportList += ["nsu": "",
                           "dataHora": "",
                           "terminal": "",
                           "estabelecimento": "",
                           "veiculo": "",
                           "km": "",
                           "equipamento": "",
                           "funcionario": "",
                           "produtos": "",
                           "precoUnitario": "",
                           "valor": "",
                           "litros": "",
                           "unidade": "",
                           "tipoTransacao": "",
                           "statusTransacao": ""
            ]


            // Inclui linha com totalizador de valor da transação
            reportList += [

                    "nsu": "",
                    "dataHora": "",
                    "terminal": "",
                    "estabelecimento": "",
                    "veiculo": "",
                    "km": "",
                    "equipamento": "",
                    "funcionario": "",
                    "produtos": "TOTAL GERAL",
                    "precoUnitario": "",
                    "valor": Util.formatCurrency(totalValor),
                    "litros": "",
                    "cliente": "",
                    "unidade": "",
                    "tipoTransacao": "",
                    "statusTransacao": ""
            ]


            List fields = [
                            "nsu",
                            "dataHora",
                            "terminal",
                            "estabelecimento",
                            "veiculo",
                            "km",
                            "equipamento",
                            "funcionario",
                            "produtos",
                            "valor",
                            "litros",
                            "precoUnitario",
                            "cliente",
                            "unidade",
                            "tipoTransacao",
                            "statusTransacao"
                        ]

            Map labels = [

                            "nsu": "NSU",
                            "dataHora": "Data/Hora",
                            "terminal": "Terminal",
                            "estabelecimento": "Estabelecimento",
                            "veiculo": "Veículo",
                            "km": "Hodômetro",
                            "equipamento": "Equipamento",
                            "funcionario": "Funcionário",
                            "valor": "Valor Total",
                            "produtos": "Produtos",
                            "litros": "Qtde Litros",
                            "precoUnitario": "Preço Unitário",
                            "cliente": "cliente",
                            "unidade": "Unidade",
                            "tipoTransacao": "Tipo Transação",
                            "statusTransacao": "Status"

                        ]

           Map parameters = [title: "Histórico de Transações Frota", "column.widths": [0.2, 0.3, 0.5]]


            exportService.export(params.f,
                                response.outputStream,
                    cabecalhoHistoricoFrotaRelatorio+reportList,
                                fields,
                                labels,
                                [:],
                                ['header.enabled': false])

            return

        }

        [
            historicoFrotaList: historicoFrotaService.list(params),
            historicoFrotaCount: historicoFrotaService.count(params)
        ]
    }


}

