package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Util

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
            if (params.empresa) {
                cabecalho.placa = "Empresa Cliente"
                Rh empresaCliente = Rh.get(params.empresa.toLong())
                cabecalho.modelo = empresaCliente.nomeFantasia
            }
            cabecalhoDemonstrativoRelatorio << cabecalho


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

            exportService.export(params.f, response.outputStream, cabecalhoDemonstrativoRelatorio + demonstrativoDesempenhoReport, fields, labels, [:], ['header.enabled': false])

            return
        }

        [desempenhoList: demonstrativoDesempenhoService.list(params), desempenhoCount: demonstrativoDesempenhoService.count(params)]

    }
}
