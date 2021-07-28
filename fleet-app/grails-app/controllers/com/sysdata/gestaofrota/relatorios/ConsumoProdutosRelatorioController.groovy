package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.Transacao
import grails.core.GrailsApplication

class ConsumoProdutosRelatorioController {

    def exportService

    ConsumoProdutosRelatorioService ConsumoProdutosRelatorioService

    def index() {
        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0

        if (params.f && params.f != 'html') {

            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=consumoCombustivel-${new Date().format('yyMMddHHmmss')}.${params.extension}")

            consumoReport = consumoReport.collect {
                                [
                                    "placa": it[0],
                                    "marca": it[1],
                                    "modelo": it[2],
                                    "rh": it[3],
                                    "unidade": it[4],
                                    "produto": it[5],
                                    "consumo": it[6],
                                    "quilometragem": it[7],
                                ]
                            }

            def fields = [
                            "placa",
                            "marca",
                            "modelo",
                            "rh",
                            "unidade",
                            "produto",
                            "consumo",
                            "quilometragem"
                        ]

            def labels = [
                            "placa"         : "Placa",
                            "marca"         : "Marca",
                            "modelo"        : "Modelo",
                            "rh"            : "Empresa",
                            "unidade"       : "Unidade",
                            "produto"       : "Produto",
                            "consumo"       : "Consumo(lts)",
                            "quilometragem" : "KM Percorrida"
                        ]

            exportService.export(params.f, response.outputStream, consumoReport, fields, labels, [:], [:])

            return
        }

        [consumoList: ConsumoProdutosRelatorioService.list(params), consumoCount: ConsumoProdutosRelatorioService.count(params)]



    }
}
