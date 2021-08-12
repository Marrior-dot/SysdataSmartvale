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
            cabecalho.dataHora = "EMISSAO"
            cabecalho.nsu = new Date().format('dd/MM/yyyy')
            cabecalhoHistoricoFrotaRelatorio << cabecalho

            def cabecalho1 = [:]
            if (params.empresa) {
                cabecalho1.dataHora = "EMPRESA"
                Rh empresaCliente = Rh.get(params.empresa.toLong())
                cabecalho1.nsu = empresaCliente.nomeFantasia
                cabecalhoHistoricoFrotaRelatorio << cabecalho1
            }
            def cabecalho2 = [:]
            if (params.unidade) {
                cabecalho2.dataHora = "UNIDADE"
                Unidade unidade = Unidade.get(params.unidade.toLong())
                cabecalho2.nsu = unidade.nome
                cabecalhoHistoricoFrotaRelatorio << cabecalho2
            }
            def cabecalho3 = [:]
            if (params.dataFim) {
                cabecalho3.dataHora = "DATA FIM"
                cabecalho3.nsu = params.dataFim
                cabecalhoHistoricoFrotaRelatorio << cabecalho3
            }
            def cabecalho4 = [:]
            if (params.placa) {
                cabecalho4.dataHora = "PLACA"
                cabecalho4.nsu = params.placa
                cabecalhoHistoricoFrotaRelatorio << cabecalho4
            }
            def cabecalho5 = [:]
            if (params.codigo) {
                cabecalho5.dataHora = "PLACA"
                cabecalho5.nsu = params.placa
                cabecalhoHistoricoFrotaRelatorio << cabecalho5
            }
            def cabecalho6 = [:]
            if (params.dataInicio) {
                cabecalho6.dataHora = "DATA INICIO"
                cabecalho6.nsu = params.dataInicio
                cabecalhoHistoricoFrotaRelatorio << cabecalho6
            }
            def cabecalho7 = [:]
            if (params.dataFim) {
                cabecalho7.dataHora = "DATA FIM"
                cabecalho7.nsu = params.dataFim
                cabecalhoHistoricoFrotaRelatorio << cabecalho7
            }

            def cabecalho8 = [:]
            cabecalho8.dataHora = ""
            cabecalhoHistoricoFrotaRelatorio << cabecalho8

            def cabecalho9 = [:]
            cabecalho9.dataHora = "DATA/HORA"
            cabecalho9.nsu = "NSU"
            cabecalho9.terminal = "TERMINAL"
            cabecalho9.estabelecimento = "ESTABELECIMENTO"
            cabecalho9.veiculo = "VEICULO"
            cabecalho9.km = "HODOMETRO"
            cabecalho9.equipamento = "EQUIPAMENTO"
            cabecalho9.funcionario = "FUNCIONARIO"
            cabecalho9.produtos = "VALOR TOTAL"
            cabecalho9.precoUnitario = "PRODUTOS"
            cabecalho9.valor = "QTD LITROS"
            cabecalho9.litros = "PREÇO UNITARIO"
            cabecalho9.cliente = "CLIENTE"
            cabecalho9.unidade = "UNIDADE"
            cabecalho9.tipoTransacao = "TIPO TRANSACAO"
            cabecalho9.statusTransacao = "STATUS"
            cabecalhoHistoricoFrotaRelatorio << cabecalho9

            def reportList = historicoFrotaService.list(params, false)

            def totalValor = reportList.sum { it.valor }

            reportList = reportList.collect { tr ->
                                [
                                    "dataHora": tr.dateCreated.format('dd/MM/yy HH:mm:ss'),
                                    "nsu": tr.nsu,
                                    "terminal": tr.terminal,
                                    "estabelecimento": tr.estabelecimento.identificacaoResumida,
                                    "veiculo": tr.maquina.instanceOf(Veiculo) ? (tr.maquina as Veiculo).identificacaoCompacta : '',
                                    "km": tr.quilometragem,
                                    "equipamento": tr.maquina.instanceOf(Equipamento) ? (tr.maquina as Equipamento).identificacaoCompacta : '',
                                    "funcionario": "(${tr.participante.matricula}) ${tr.participante.nome}",
                                    "produtos": "${tr.produtos*.produto.nome.join('')}",
                                    "precoUnitario": 'R$'+tr.precoUnitario,
                                    "valor": 'R$'+tr.valor,
                                    "litros": tr.qtd_litros,
                                    "cliente": tr.cartao.portador.unidade.rh.nome,
                                    "unidade": tr.cartao.portador.unidade.nome,
                                    "tipoTransacao": tr.tipo.nome,
                                    "statusTransacao": tr.statusControle.nome,

                                ]
                            }

            reportList += [
                           "dataHora": "",
                           "nsu": "",
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

                    "dataHora": "",
                    "nsu": "",
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
                            "dataHora",
                            "nsu",
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

                            "dataHora": "Data/Hora",
                            "nsu": "NSU",
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

