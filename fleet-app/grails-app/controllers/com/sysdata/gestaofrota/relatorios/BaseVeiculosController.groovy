package com.sysdata.gestaofrota.relatorios

import grails.core.GrailsApplication
import com.sysdata.gestaofrota.Veiculo

class BaseVeiculosController {

    def exportService

    GrailsApplication grailsApplication

    def index() {

        StringBuilder sb = new StringBuilder()

        sb.append("""
            select
                v.placa,
                v.marca.abreviacao,
                v.modelo,
                v.unidade.rh.nome,
                v.unidade.nome,
                v.anoFabricacao,
                v.hodometro

            from

                Veiculo as v


            """)

        if (params.placa)
            sb.append(" and v.placa = ${params.placa}")

        else if (params.unidade) {
            sb.append(" and v.unidade.id = ${params.unidade as long}")
        }

        if (params.dataInicio && params.dataFim) {
            sb.append(""" and t.dateCreated >= ${params.date('dataInicio', 'dd/MM/yyyy')} and
                t.dateCreated <= ${params.date('dataFim', 'dd/MM/yyyy') + 1} """)
        }

            sb.append("""
            group by
                v.placa,
                v.marca.abreviacao,
                v.modelo,
                v.unidade.rh.nome,
                v.unidade.nome,
                v.anoFabricacao,
                v.hodometro

            order by
                v.placa
            """)


        if (params.f && params.f != 'html') {

            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=baseVeiculos-${new Date().format('yyMMddHHmmss')}.${params.extension}")

            def consumoReport = Veiculo.executeQuery(sb.toString(),
                    [])

            consumoReport = consumoReport.collect {
                [
                        "placa": it[0],
                        "marca": it[1],
                        "modelo": it[2],
                        "rh": it[3],
                        "unidade": it[4],
                        "ano_fabricacao": it[5],
                        "hodometro": it[6],
                ]
            }

            def fields = ["placa", "marca", "modelo", "rh", "unidade", "ano_fabricacao", "hodometro"]

            def labels = [
                    "placa"   : "Placa",
                    "marcar"  : "Marca",
                    "modelo"  : "Modelo",
                    "rh"      : "Empresa",
                    "unidade" : "Unidade",
                    "ano_fabricacao" : "Ano Fabricação",
                    "hodometro" : "Hodometro(Km)"
            ]

            exportService.export(params.f, response.outputStream, consumoReport, fields, labels, [:], [:])

            return
        }




        def baseVeiculosList = Veiculo.executeQuery(sb.toString(),[
        ], [max: params.max ? params.max as int : 10, offset: params.offset ? params.offset as int : 0] )

        def baseVeiculosCount = Veiculo.executeQuery(sb.toString(),[])
        return baseVeiculosCount

        [baseVeiculosList: baseVeiculosList, baseVeiculosCount: baseVeiculosCount, params: params]

      // [baseVeiculosList: baseVeiculosList.list(params), baseVeiculosCount: baseVeiculosCount.count()]

    }
}
