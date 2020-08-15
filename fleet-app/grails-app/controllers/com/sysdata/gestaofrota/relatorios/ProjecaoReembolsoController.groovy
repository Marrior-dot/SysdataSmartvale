package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Util
import grails.core.GrailsApplication

class ProjecaoReembolsoController {

    GrailsApplication grailsApplication
    def exportService
    ProjecaoReembolsoService projecaoReembolsoService

    def index() {
        params.max = params.max ? params.max as int : 0
        params.offset = params.offset ? params.offset as int : 0


        if (params.f && params.f != 'html') {

            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=projecaoReembolso-${new Date().format('yyMMdd')}.${params.extension}")

            def projecaoReport = projecaoReembolsoService.list(params, false)
            projecaoReport = projecaoReport.collect {
                                [
                                        "razao": it[0],
                                        "nomeFantasia": it[1],
                                        "cnpj": it[2],
                                        "data": it[3].format('dd/MM/yyyy'),
                                        "valorBruto": Util.formatCurrency(it[4]),
                                        "valorLiquido": Util.formatCurrency(it[5]),
                                        "taxaAdm": Util.formatPercentage(it[6]),
                                        "banco": it[7],
                                        "agencia": it[8],
                                        "conta": it[9],
                                        "nomeTitular": it[10],
                                        "docTitular": it[11]
                                ]
                            }
            
            def labels = [
                            "razao": "Razão Social",
                            "nomeFantasia": "Nome Fantasia",
                            "cnpj": "CNPJ",
                            "data": "Data Programada",
                            "valorBruto": "Valor Bruto",
                            "valorLiquido": "Valor Reembolsar",
                            "taxaAdm": "Taxa Adm (%)",
                            "banco": "Banco",
                            "agencia": "Agência",
                            "conta": "Conta",
                            "nomeTitular": "Nome Titular",
                            "docTitular": "Doc Titular"
                        ]

            def fields = [
                            "razao",
                            "nomeFantasia",
                            "cnpj",
                            "data",
                            "valorBruto",
                            "valorLiquido",
                            "taxaAdm",
                            "banco",
                            "agencia",
                            "conta",
                            "nomeTitular",
                            "docTitular"
            ]

            exportService.export(params.f, response.outputStream, projecaoReport, fields, labels, [:], [:])

            return
        }


        def projecaoList = projecaoReembolsoService.list(params)
        def projecaoCount = projecaoReembolsoService.count(params)

        [projecaoList: projecaoList, projecaoCount: projecaoCount]

    }

}