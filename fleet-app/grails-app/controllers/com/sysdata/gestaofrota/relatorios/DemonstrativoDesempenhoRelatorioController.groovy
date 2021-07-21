package com.sysdata.gestaofrota.relatorios

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

            def demonstrativoDesempenhoReport = demonstrativoDesempenhoService.list(params, false)
            //D.Lyra 20/07/2021
            def totalKmRod = demonstrativoDesempenhoReport.sum { it[5] }
            demonstrativoDesempenhoReport = demonstrativoDesempenhoReport.collect {
                [
                        "placa": it[0],
                        "modelo": it[1],
                        "funcionario": it[2],
                        "empresa": it[3],
                        "unidade": it[4],
                        "kmRodados": it[5],
                        "desempenho": it[6]

                ]
            }

            demonstrativoDesempenhoReport += [

                    "placa": "",
                    "modelo": "",
                    "funcionario": "",
                    "empresa": "",
                    "unidade": "TOTAL GERAL",
                    "kmRodados": totalKmRod,
                    "desempenho": ""
            ]

            def labels = [
                    "placa": "Placa",
                    "modelo": "Marca/Modelo",
                    "funcionario": "Funcionário",
                    "empresa": "Empresa",
                    "unidade": "Unidade",
                    "kmRodados": "Km Rodados",
                    "desempenho": "Desempenho (km/l)"
            ]

            def fields = [
                    "placa",
                    "modelo",
                    "funcionario",
                    "empresa",
                    "unidade",
                    "kmRodados",
                    "desempenho"
            ]

            exportService.export(params.f, response.outputStream, demonstrativoDesempenhoReport, fields, labels, [:], [:])

            return
        }

        [desempenhoList: demonstrativoDesempenhoService.list(params), desempenhoCount: demonstrativoDesempenhoService.count(params)]

    }
}
