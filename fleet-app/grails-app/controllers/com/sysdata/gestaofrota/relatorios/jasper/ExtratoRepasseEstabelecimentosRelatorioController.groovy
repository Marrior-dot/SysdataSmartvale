package com.sysdata.gestaofrota.relatorios.jasper

import com.sysdata.gestaofrota.Util
import grails.core.GrailsApplication

class ExtratoRepasseEstabelecimentosRelatorioController extends JasperBaseRelatorioController {

    GrailsApplication grailsApplication

    def index() { }

    def list() {
        if (params.dataInicial && params.dataFinal) {
            def pars = [:]
            params.DataInicial = new java.sql.Date(params.date('dataInicial', 'dd/MM/yyyy').getTime())
            params.DataFinal = new java.sql.Date(params.date('dataFinal', 'dd/MM/yyyy').getTime())
            //params.logoBanparaBranca = grailsApplication.config.project.relatorios.logoBanparaBranco
            params.logoBanparaBranca = "/home/diego/tmp/banpara/renda/marituba/logo/banpara_novo.png"
            if (!params.CNPJ)
                params.CNPJ = null
            else
                params.CNPJ = Util.cnpjToRaw(params.CNPJ)

            if (!params.nomeFatasia)
                params.nomeFatasia = null
            if (!params.cliente)
                params.cliente = null

            println(params)
            OutputStream outputStream = response.outputStream
            try {
                exportToPdf(grailsApplication, "rel_ExtratoRepasseEstabelecimentosComerciais", params, outputStream)
            }
            finally {
                outputStream.flush()
                outputStream.close()
            }
        }else
            flash.error = "Obrigatório informar o intervalo de datas!"

    }
}