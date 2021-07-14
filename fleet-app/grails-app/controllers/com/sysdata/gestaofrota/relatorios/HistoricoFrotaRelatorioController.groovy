package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Equipamento
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
                                    "unidade": tr.cartao.portador.unidade.nome,
                                    "tipoTransacao": tr.tipo.nome,
                                    "statusTransacao": tr.statusControle.nome,

                                ]
                            }

            reportList += ["nsu": "", "dataHora": "", "terminal": "", "estabelecimento": "", "veiculo": "", "km": "", "equipamento": "", "funcionario": "",
                    "produtos": "", "precoUnitario": "", "valor": "", "litros": "", "unidade": "", "tipoTransacao": "", "statusTransacao": ""
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
                            "unidade": "Unidade",
                            "tipoTransacao": "Tipo Transação",
                            "statusTransacao": "Status"

                        ]

           Map parameters = [title: "Histórico de Transações Frota", "column.widths": [0.2, 0.3, 0.5]]


            exportService.export(params.f,
                                response.outputStream,
                                reportList,
                                fields,
                                labels, [:], [:])

            return

        }

        [
            historicoFrotaList: historicoFrotaService.list(params),
            historicoFrotaCount: historicoFrotaService.count(params)
        ]
    }


}

