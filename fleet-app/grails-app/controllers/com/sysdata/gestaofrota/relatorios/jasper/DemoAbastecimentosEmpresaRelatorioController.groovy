package com.sysdata.gestaofrota.relatorios.jasper

import com.sysdata.gestaofrota.Util
import grails.core.GrailsApplication

class DemoAbastecimentosEmpresaRelatorioController extends JasperBaseRelatorioController {

    GrailsApplication grailsApplication

    def index() { }

    def list() {
        if (params.dataInicial && params.dataFinal) {
            def pars = [:]
            params.DataInicial = new java.sql.Date(params.date('dataInicial', 'dd/MM/yyyy').getTime())
            params.DataFinal = new java.sql.Date(params.date('dataFinal', 'dd/MM/yyyy').getTime())
            //params.logoBanparaBranca = grailsApplication.config.project.relatorios.logoBanparaBranco
            params.logoBanparaBranca = "/home/diego/tmp/banpara/renda/marituba/logo/banpara_novo.png"
            if (!params.cartao)
                params.cartao = null

            if (!params.estabelecimento)
                params.estabelecimento = null

            println(params)
            OutputStream outputStream = response.outputStream
            try {
                exportToPdf(grailsApplication, "rel_DemoAbastecimentoEmpresa", params, outputStream)
            }
            finally {
                outputStream.flush()
                outputStream.close()
            }
        }else
            flash.error = "Obrigatório informar o intervalo de datas!"

    }
}