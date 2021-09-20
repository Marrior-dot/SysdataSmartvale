package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Unidade
import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.Transacao
import grails.core.GrailsApplication

class ConsumoProdutosRelatorioController {

    def exportService

    GrailsApplication grailsApplication


    ConsumoProdutosRelatorioService consumoProdutosRelatorioService

    def index() {
        params.max = params.max ? params.max as int : 10

        if (params.f && params.f != 'html') {

            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=consumoCombustivel-${new Date().format('yyMMddHHmmss')}.${params.extension}")

            def cabecalhoDemonstrativoRelatorio = []

            def cabecalho = [:]
            cabecalho.placa = "EMISSAO"
            cabecalho.marca = new Date().format('dd/MM/yyyy')
            cabecalhoDemonstrativoRelatorio << cabecalho

            def cabecalho1 = [:]
            if (params.empresa) {
                cabecalho1.placa = "EMPRESA"
                Rh empresaCliente = Rh.get(params.empresa.toLong())
                cabecalho1.marca = empresaCliente.nomeFantasia
                cabecalhoDemonstrativoRelatorio << cabecalho1
            }
            def cabecalho2 = [:]
            if (params.unidade) {
                cabecalho2.placa = "UNIDADE"
                Unidade unidade = Unidade.get(params.unidade.toLong())
                cabecalho2.marca = unidade.nome
                cabecalhoDemonstrativoRelatorio << cabecalho2
            }
            def cabecalho3 = [:]
            if (params.placa) {
                cabecalho3.placa = "PLACA"
                cabecalho3.marca = params.placa
                cabecalhoDemonstrativoRelatorio << cabecalho3
            }
            def cabecalho4 = [:]
            if (params.dataInicio) {
                cabecalho4.placa = "DT. Inicio"
                cabecalho4.marca = params.dataInicio
                cabecalhoDemonstrativoRelatorio << cabecalho4
            }
            def cabecalho5 = [:]
            if (params.dataFim) {
                cabecalho5.placa = "DT. Fim"
                cabecalho5.marca = params.dataFim
                cabecalhoDemonstrativoRelatorio << cabecalho5
            }

            def cabecalho6 = [:]
            cabecalho6.placa = ""
            cabecalhoDemonstrativoRelatorio << cabecalho6

            def cabecalho7 = [:]
            cabecalho7.placa = "PLACA"
            cabecalho7.marca = "MARCA/MODELO"
            cabecalho7.rh = "CLIENTE"
            cabecalho7.unidade = "UNIDADE"
            cabecalho7.produto = "PRODUTO"
            cabecalho7.consumo = "LTS ABASTECIDOS"
            cabecalho7.quilometragem = "DESEMPENHO (km/l)"
            cabecalhoDemonstrativoRelatorio << cabecalho7

            def consumoReport = consumoProdutosRelatorioService.list(params)

            consumoReport = consumoReport.collect {
                [
                        "placa": it[0],
                        "marca": "${it[1]} / ${it[2]}",
                        "rh": it[3],
                        "unidade": it[4],
                        "produto": it[5],
                        "consumo": it[6],
                        "quilometragem": it[7],
                ]
            }
            consumoReport += [
                    "placa"         : "",
                    "marca"         : "",
                    "rh"            : "",
                    "unidade"       : "",
                    "produto"       : "",
                    "consumo"       : "",
                    "quilometragem" : ""

                    ]

            def fields = [
                    "placa",
                    "marca",
                    "rh",
                    "unidade",
                    "produto",
                    "consumo",
                    "quilometragem"
            ]

            def labels = [
                    "placa"         : "Placa",
                    "marca"         : "Marca",
                    "rh"            : "Empresa",
                    "unidade"       : "Unidade",
                    "produto"       : "Produto",
                    "consumo"       : "Consumo(lts)",
                    "quilometragem" : "KM Percorrida"
            ]

            exportService.export(params.f, response.outputStream, cabecalhoDemonstrativoRelatorio+consumoReport, fields, labels, [:],
                                ['header.enabled': false])

            return
        }
        [
            consumoList: consumoProdutosRelatorioService.list(params),
            consumoCount: consumoProdutosRelatorioService.count(params),
            params: params
        ]
    }
}
