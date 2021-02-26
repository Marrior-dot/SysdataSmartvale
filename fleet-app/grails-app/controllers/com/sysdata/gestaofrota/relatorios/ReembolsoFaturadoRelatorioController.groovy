package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Util

class ReembolsoFaturadoRelatorioController {

    def exportService

    ReembolsoFaturadoService reembolsoFaturadoService

    def index() {
        params.max = params.max ? params.max as int : 0
        params.offset = params.offset ? params.offset as int : 0


        if (params.f && params.f != 'html') {

            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=reembolsoFaturado-${new Date().format('yyMMdd')}.${params.extension}")

            def reembolsoFaturadoReport = reembolsoFaturadoService.list(params, false)
            reembolsoFaturadoReport = reembolsoFaturadoReport.collect {
                [
                        "razao": it[0],
                        "nomeFantasia": it[1],
                        "cnpj": it[2],
                        "data": it[3].format('dd/MM/yyyy'),
                        "valorBruto": Util.formatCurrency(it[4]),
                        "valorLiquido": Util.formatCurrency(it[5]),
                        "taxaAdm": Util.formatPercentage(it[6]),
                        "valorTaxaAdm": Util.formatCurrency(it[7]),
                        "status": it[8]?.nome,
                        "banco": it[9],
                        "agencia": it[10],
                        "conta": it[11],
                        "nomeTitular": it[12],
                        "docTitular": it[13]
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
                    "valorTaxaAdm": "Taxa Adm (R\$)",
                    "status": "Situação",
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
                    "valorTaxaAdm",
                    "status",
                    "banco",
                    "agencia",
                    "conta",
                    "nomeTitular",
                    "docTitular"
            ]

            exportService.export(params.f, response.outputStream, reembolsoFaturadoReport, fields, labels, [:], [:])

            return
        }


        [reembolsoList: reembolsoFaturadoService.list(params), reembolsoCount: reembolsoFaturadoService.count(params)]

    }
}
