package com.sysdata.gestaofrota.relatorios.jasper

import com.sysdata.gestaofrota.Util
import grails.core.GrailsApplication

class VendasLojistaRelatorioController extends JasperBaseRelatorioController {

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
            /*else
                params.CNPJ = Util.cnpjToRaw(params.CNPJ)*/

            OutputStream outputStream = response.outputStream
            try {
                exportToPdf(grailsApplication, "rel_VendasLojista", params, outputStream)
            } catch (e) {
                log.error "Erro ao gerar relatório: $e.message"
                e.printStackTrace()

            } finally {
                outputStream.flush()
                outputStream.close()
            }
        }else
            flash.error = "Obrigatório informar o intervalo de datas!"


    }
}