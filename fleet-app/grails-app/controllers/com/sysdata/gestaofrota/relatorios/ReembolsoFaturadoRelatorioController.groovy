package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Util

class ReembolsoFaturadoRelatorioController {

    def exportService

    ReembolsoFaturadoService reembolsoFaturadoService

    def index() {
        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0


        if (params.f && params.f != 'html') {

            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=reembolsoFaturado-${new Date().format('yyMMdd')}.${params.extension}")


            def cabecalhoReembolsoFaturadoRelatorio = []
            def cabecalho = [:]
            cabecalho.razao = "EMISSAO"
            cabecalho.nomeFantasia = new Date().format('dd/MM/yyyy')
            cabecalhoReembolsoFaturadoRelatorio << cabecalho

            def cabecalho1 = [:]
            if (params.cnpj) {
                cabecalho1.razao = "CNPJ"
                cabecalho1.nomeFantasia = params.cnpj
                cabecalhoReembolsoFaturadoRelatorio << cabecalho1
            }

            def cabecalho2 = [:]
            if (params.dataInicio) {
                cabecalho2.razao = "DT. Inicio"
                cabecalho2.nomeFantasia = params.dataInicio
                cabecalhoReembolsoFaturadoRelatorio << cabecalho2
            }
            def cabecalho3 = [:]
            if (params.dataFim) {
                cabecalho3.razao = "DT. Fim"
                cabecalho3.nomeFantasia = params.dataFim
                cabecalhoReembolsoFaturadoRelatorio << cabecalho3
            }

            def cabecalho4 = [:]
            cabecalho4.razao = ""
            cabecalhoReembolsoFaturadoRelatorio << cabecalho4

            def cabecalho5 = [:]
            cabecalho5.razao = "RAZAO"
            cabecalho5.nomeFantasia = "NOME FANT."
            cabecalho5.cnpj = "CNPJ"
            cabecalho5.data = "DATA PROGR."
            cabecalho5.valorBruto = "VALOR BRUTO"
            cabecalho5.valorLiquido = "VALOR LIQ."
            cabecalho5.taxaAdm = "TAXA ADM."
            cabecalho5.valorTaxaAdm = "VALOR TAXA ADM."
            cabecalho5.status = "STATUS"
            //cabecalhoReembolsoFaturadoRelatorio << cabecalho
            //cabecalhoReembolsoFaturadoRelatorio << cabecalho1
            //cabecalhoReembolsoFaturadoRelatorio << cabecalho2
            cabecalhoReembolsoFaturadoRelatorio << 5

            def reembolsoFaturadoReport = reembolsoFaturadoService.list(params, false)
            //D.Lyra 20/07/2021
            //def totalValorB = reembolsoFaturadoReport.sum { it[4] }
            def totalValorL = reembolsoFaturadoReport.sum { it[5] }
            //def totalValorTAdm = reembolsoFaturadoReport.sum { it[7] }
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
                        "status": it[8]?.nome
                        /*"banco": it[9],
                        "agencia": it[10],
                        "conta": it[11],
                        "nomeTitular": it[12],
                        "docTitular": it[13]*/
                ]
            }

            reembolsoFaturadoReport += [

                    "razao": "",
                    "nomeFantasia": "",
                    "cnpj": "",
                    "data": "Total Geral",
                    "valorBruto": "",//Util.formatCurrency(totalValorB),
                    "valorLiquido": Util.formatCurrency(totalValorL),
                    "taxaAdm": "",
                    "valorTaxaAdm": "",//Util.formatCurrency(totalValorTAdm),
                    "status": ""
                    /*"banco": "",
                    "agencia": "",
                    "conta": "",
                    "nomeTitular": it[12],
                    "docTitular": it[13]*/
            ]

            def labels = [
                    "razao": "Razão Social",
                    "nomeFantasia": "Nome Fantasia",
                    "cnpj": "CNPJ",
                    "data": "Data Programada",
                    "valorBruto": "Valor Bruto",
                    "valorLiquido": "Valor Reembolsar",
                    "taxaAdm": "Taxa Adm (%)",
                    "valorTaxaAdm": "Taxa Adm (R\$)",
                    "status": "Situação"
                    /*"banco": "Banco",
                    "conta": "Conta",
                    "nomeTitular": "Nome Titular",
                    "docTitular": "Doc Titular"*/
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
                    "status"
                    /*"banco",
                    "agencia",
                    "conta",
                    "nomeTitular",
                    "docTitular"*/
            ]
            //exportService.export(params.f, response.outputStream, reembolsoFaturadoReport, fields, labels, [:], [:])
            exportService.export(params.f, response.outputStream, cabecalhoReembolsoFaturadoRelatorio+reembolsoFaturadoReport, fields, labels, [:], ['header.enabled': false])

            return
        }


        [reembolsoList: reembolsoFaturadoService.list(params), reembolsoCount: reembolsoFaturadoService.count(params)]

    }
}
