package com.sysdata.gestaofrota.relatorios.jasper

import com.sysdata.gestaofrota.Util
import grails.core.GrailsApplication

class DemoAbastecimentosEstabelecimentoRelatorioController extends JasperBaseRelatorioController {

    GrailsApplication grailsApplication

    def index() { }

    def list() {
        if (params.dataInicial && params.dataFinal) {
            def pars = [:]
            params.DataInicial = new java.sql.Date(params.date('dataInicial', 'dd/MM/yyyy').getTime())
            params.DataFinal = new java.sql.Date(params.date('dataFinal', 'dd/MM/yyyy').getTime())
            params.logoBanparaBranca = grailsApplication.config.projeto.relatorios.logoBanparaBranco


            if (!params.CNPJ)
                params.CNPJ = null

            println(params)
            OutputStream outputStream = response.outputStream
            try {
                exportToPdf(grailsApplication, "rel_DemoAbastecimentoEstabelecimentos", params, outputStream)

            }
            finally {
                outputStream.flush()
                outputStream.close()
            }
        }else
            flash.error = "Obrigatório informar o intervalo de datas!"

    }
}