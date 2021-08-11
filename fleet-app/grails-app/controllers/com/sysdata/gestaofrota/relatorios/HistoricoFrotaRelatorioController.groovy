package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Equipamento
import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Unidade
import com.sysdata.gestaofrota.Util
import com.sysdata.gestaofrota.Veiculo

class HistoricoFrotaRelatorioController {

    def exportService
    HistoricoFrotaService historicoFrotaService

    private def setHeader(pars) {
        def cabecalhoHistoricoFrotaRelatorio = []
        def cabecalho = [:]
        cabecalho.dataHora = "Data/Hora Emissão"
        cabecalho.nsu = new Date().format('dd/MM/yy HH:mm')
        cabecalhoHistoricoFrotaRelatorio << cabecalho

        def cabecalho1 = [:]
        if (pars.empresa) {
            cabecalho1.dataHora = "Empresa Cliente"
            Rh empresaCliente = Rh.get(pars.empresa.toLong())
            cabecalho1.nsu = empresaCliente.nomeFantasia
            cabecalhoHistoricoFrotaRelatorio << cabecalho1
        }
        def cabecalho2 = [:]
        if (pars.unidade) {
            cabecalho2.dataHora = "Unidade"
            Unidade unidade = Unidade.get(pars.unidade.toLong())
            cabecalho2.nsu = unidade.nome
            cabecalhoHistoricoFrotaRelatorio << cabecalho2
        }
        def cabecalho4 = [:]
        if (pars.placa) {
            cabecalho4.dataHora = "Placa"
            cabecalho4.nsu = pars.placa
            cabecalhoHistoricoFrotaRelatorio << cabecalho4
        }
        def cabecalho5 = [:]
        if (pars.codigo) {
            cabecalho5.dataHora = "Cod. Equipamento"
            cabecalho5.nsu = pars.codigo
            cabecalhoHistoricoFrotaRelatorio << cabecalho5
        }
        def cabecalho6 = [:]
        if (pars.dataInicio) {
            cabecalho6.dataHora = "Data Início"
            cabecalho6.nsu = pars.dataInicio
            cabecalhoHistoricoFrotaRelatorio << cabecalho6
        }
        def cabecalho7 = [:]
        if (pars.dataFim) {
            cabecalho7.dataHora = "Data Fim"
            cabecalho7.nsu = pars.dataFim
            cabecalhoHistoricoFrotaRelatorio << cabecalho7
        }

        def cabecalho8 = [:]
        cabecalho8.dataHora = ""
        cabecalhoHistoricoFrotaRelatorio << cabecalho8

        def cabecalho9 = [:]
        cabecalho9.dataHora = "Data/Hora"
        cabecalho9.nsu = "NSU Aut"
        cabecalho9.terminal = "Terminal"
        cabecalho9.estabelecimento = "Estabelecimento"
        cabecalho9.veiculo = "Veículo"
        cabecalho9.km = "Hodômetro"
        cabecalho9.equipamento = "Equipamento"
        cabecalho9.funcionario = "Funcionário"
        cabecalho9.produtos = "Produtos"
        cabecalho9.precoUnitario = "Preço Unit."
        cabecalho9.valor = "Valor"
        cabecalho9.litros = "Qtde Lts"
        cabecalho9.cliente = "Cliente"
        cabecalho9.unidade = "Unidade"
        cabecalho9.tipoTransacao = "Tipo Transação"
        cabecalho9.statusTransacao = "Status"
        cabecalhoHistoricoFrotaRelatorio << cabecalho9

        return cabecalhoHistoricoFrotaRelatorio
    }



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
                        "dataHora": tr.dateCreated.format('dd/MM/yy HH:mm:ss'),
                        "nsu": tr.nsu,
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


            Map parameters = [title: "Histórico de Transações Frota", "column.widths": [0.2, 0.3, 0.5]]


            exportService.export(params.f,
                    response.outputStream,
                    setHeader(params) + reportList,
                    fields,
                    null,
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

