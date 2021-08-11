package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Util
import grails.core.GrailsApplication

class ProjecaoReembolsoRelatorioController {

    GrailsApplication grailsApplication
    def exportService
    ProjecaoReembolsoService projecaoReembolsoService

    def index() {
        params.max = params.max ? params.max as int : 0
        params.offset = params.offset ? params.offset as int : 0


        if (params.f && params.f != 'html') {

            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=projecaoReembolso-${new Date().format('yyMMdd')}.${params.extension}")

            def cabecalhoProjecaoReembolsoRelatorio = []
            def cabecalho = [:]
            cabecalho.razao = "EMISSAO"
            cabecalho.nomeFantasia = new Date().format('dd/MM/yyyy')
            cabecalhoProjecaoReembolsoRelatorio << cabecalho

            def cabecalho1 = [:]
            if (params.cnpj) {
                cabecalho1.razao = "CNPJ"
                cabecalho1.nomeFantasia = params.cnpj
                cabecalhoProjecaoReembolsoRelatorio << cabecalho1
            }

            def cabecalho2 = [:]
            if (params.dataInicio) {
                cabecalho2.razao = "DT. Inicio"
                cabecalho2.nomeFantasia = params.dataInicio
                cabecalhoProjecaoReembolsoRelatorio << cabecalho2
            }
            def cabecalho3 = [:]
            if (params.dataFim) {
                cabecalho3.razao = "DT. Fim"
                cabecalho3.nomeFantasia = params.dataFim
                cabecalhoProjecaoReembolsoRelatorio << cabecalho3
            }

            def cabecalho4 = [:]
            cabecalho4.razao = ""
            cabecalhoProjecaoReembolsoRelatorio << cabecalho4

            def cabecalho5 = [:]
            cabecalho5.razao = "RAZAO"
            cabecalho5.nomeFantasia = "NOME FANT."
            cabecalho5.cnpj = "CNPJ"
            cabecalho5.data = "DATA PROGR."
            cabecalho5.valorBruto = "VALOR BRUTO"
            cabecalho5.valorLiquido = "VALOR LIQ."
            cabecalho5.taxaAdm = "TAXA ADM."
            cabecalho5.valorTaxaAdm = "VALOR TAXA ADM."
            cabecalho5.banco = "BANCO"
            cabecalho5.agencia = "AGENCIA"
            cabecalho5.conta = "CONTA"
            cabecalho5.nomeTitular = "NOME TITULAR"
            cabecalho5.docTitular = "DOC. TITULAR"
            cabecalhoProjecaoReembolsoRelatorio << cabecalho5

            def projecaoReport = projecaoReembolsoService.list(params, false)
            def totalValorL = projecaoReport.sum { it[5] }
            projecaoReport = projecaoReport.collect {
                                [
                                        "razao": it[0],
                                        "nomeFantasia": it[1],
                                        "cnpj": it[2],
                                        "data": it[3].format('dd/MM/yyyy'),
                                        "valorBruto": Util.formatCurrency(it[4]),
                                        "valorLiquido": Util.formatCurrency(it[5]),
                                        "taxaAdm": Util.formatPercentage(it[6]),
                                        "valorTaxaAdm": Util.formatCurrency(it[4] - it[5]),
                                        "banco": it[7],
                                        "agencia": it[8],
                                        "conta": it[9],
                                        "nomeTitular": it[10],
                                        "docTitular": it[11]
                                ]
                            }

            projecaoReport += [

                    "razao": "",
                    "nomeFantasia": "",
                    "cnpj": "",
                    "data": "Total Geral",
                    "valorBruto": "",//Util.formatCurrency(totalValorB),
                    "valorLiquido": Util.formatCurrency(totalValorL),
                    "taxaAdm": "",
                    "valorTaxaAdm": "",//Util.formatCurrency(totalValorTAdm),
                    "status": "",
                    "banco": "",
                    "agencia": "",
                    "conta": "",
                    "nomeTitular": "",
                    "docTitular": ""
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
                            "banco",
                            "agencia",
                            "conta",
                            "nomeTitular",
                            "docTitular"
            ]

            exportService.export(params.f, response.outputStream, cabecalhoProjecaoReembolsoRelatorio+projecaoReport, fields, labels, [:], ['header.enabled': false])

            return
        }


        def projecaoList = projecaoReembolsoService.list(params)
        def projecaoCount = projecaoReembolsoService.count(params)

        [projecaoList: projecaoList, projecaoCount: projecaoCount]

    }

}