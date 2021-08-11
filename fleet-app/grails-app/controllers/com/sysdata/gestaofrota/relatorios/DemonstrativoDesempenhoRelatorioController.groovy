package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Unidade
import com.sysdata.gestaofrota.Util
import jxl.write.DateTime
import sun.util.calendar.BaseCalendar

import java.time.format.DateTimeFormatter

class DemonstrativoDesempenhoRelatorioController {

    def exportService

    DemonstrativoDesempenhoService demonstrativoDesempenhoService

    def index() {
        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0

        if (params.f && params.f != 'html') {

            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=DemonstrativoDesempenho-${new Date().format('yyMMdd')}.${params.extension}")

            def cabecalhoDemonstrativoRelatorio = []

            def cabecalho = [:]
            cabecalho.placa = "EMISSAO"
            cabecalho.modelo = new Date().format('dd/MM/yyyy')
            cabecalhoDemonstrativoRelatorio << cabecalho

            def cabecalho1 = [:]
            if (params.empresa) {
                cabecalho1.placa = "EMPRESA"
                Rh empresaCliente = Rh.get(params.empresa.toLong())
                cabecalho1.modelo = empresaCliente.nomeFantasia
                cabecalhoDemonstrativoRelatorio << cabecalho1
            }
            def cabecalho2 = [:]
            if (params.unidade) {
                cabecalho2.placa = "UNIDADE"
                Unidade unidade = Unidade.get(params.unidade.toLong())
                cabecalho2.modelo = unidade.nome
                cabecalhoDemonstrativoRelatorio << cabecalho2
            }
            def cabecalho3 = [:]
            if (params.placa) {
                cabecalho3.placa = "PLACA"
                cabecalho3.modelo = params.placa
                cabecalhoDemonstrativoRelatorio << cabecalho3
            }
            def cabecalho4 = [:]
            if (params.dataInicio) {
                cabecalho4.placa = "DT. Inicio"
                cabecalho4.modelo = params.dataInicio
                cabecalhoDemonstrativoRelatorio << cabecalho4
            }
            def cabecalho5 = [:]
            if (params.dataFim) {
                cabecalho5.placa = "DT. Fim"
                cabecalho5.modelo = params.dataFim
                cabecalhoDemonstrativoRelatorio << cabecalho5
            }

            def cabecalho6 = [:]
            cabecalho6.placa = ""
            cabecalhoDemonstrativoRelatorio << cabecalho6

            def cabecalho7 = [:]
            cabecalho7.placa = "Placa"
            cabecalho7.modelo = "Marca/Modelo"
            cabecalho7.funcionario = "Funcionário"
            cabecalho7.empresa = "Empresa"
            cabecalho7.unidade = "Unidade"
            cabecalho7.kmRodados = "Km Rodados"
            cabecalho7.litros = "Lts Abastecidos"
            cabecalho7.desempenho = "Desempenho (km/l)"
            //cabecalhoDemonstrativoRelatorio << cabecalho
            //cabecalhoDemonstrativoRelatorio << cabecalho1
            //cabecalhoDemonstrativoRelatorio << cabecalho2
            cabecalhoDemonstrativoRelatorio << cabecalho7



/*



            demonstrativoDesempenhoReport << [
                    "placa": "Empresa Cliente",
                    "modelo": params.empresa,
                    "funcionario": "",
                    "empresa": "",
                    "unidade": "TOTAL GERAL",
                    "kmRodados": totalKmRod,
                    "litros": "",
                    "desempenho": ""
            ]

*/


            def demonstrativoDesempenhoReport = demonstrativoDesempenhoService.list(params, false)
            //D.Lyra 20/07/2021
            //def totalKmRod = demonstrativoDesempenhoReport.sum { it[5] }


            demonstrativoDesempenhoReport = demonstrativoDesempenhoReport.collect {

                [
                    "placa": it[0],
                    "modelo": "${it[1]} / ${it[2]}",
                    "funcionario": "${it[3]} / ${it[4]}",
                    "empresa": it[5],
                    "unidade": it[6],
                    "kmRodados": it[7],
                    "litros": it[8],
                    "desempenho": it[9].round(2)
                ]
            }

            /*demonstrativoDesempenhoReport += [

                    "placa": "",
                    "modelo": "",
                    "funcionario": "",
                    "empresa": "",
                    "unidade": "TOTAL GERAL",
                    "kmRodados": totalKmRod,
                    "desempenho": ""
            ]*/

            def labels = [
                    "placa": "Placa",
                    "modelo": "Marca/Modelo",
                    "funcionario": "Funcionário",
                    "empresa": "Empresa",
                    "unidade": "Unidade",
                    "kmRodados": "Km Rodados",
                    "litros": "Lts Abastecidos",
                    "desempenho": "Desempenho (km/l)"
            ]

            def fields = [
                    "placa",
                    "modelo",
                    "funcionario",
                    "empresa",
                    "unidade",
                    "kmRodados",
                    "litros",
                    "desempenho"
            ]

            exportService.export(params.f, response.outputStream, cabecalhoDemonstrativoRelatorio+demonstrativoDesempenhoReport, fields, labels, [:], ['header.enabled': false])

            return
        }

        [desempenhoList: demonstrativoDesempenhoService.list(params), desempenhoCount: demonstrativoDesempenhoService.count(params)]

    }
}
