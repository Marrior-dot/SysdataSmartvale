package com.sysdata.gestaofrota.relatorios.jasper

import grails.core.GrailsApplication
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.view.JasperViewer

class DemonstrativoAbastecimentosRelatorioController {

    GrailsApplication grailsApplication

    def dataSource

    def index() {}

    def list() {
        //
        try {
            def jrxmlFileName = "src/main/groovy/com/sysdata/gestaofrota/relatorios/jasper/rel_DemoAbastecimentoEstabelecimentos.jrxml"
            JasperReport objJReport = JasperCompileManager.compileReport(jrxmlFileName);

            params.Logo = grailsApplication.config.projeto.jasperImages
            params.DtInicial = params.date('dataInicial', 'dd/MM/yyyy')
            params.DtFinal = params.date('dataFinal', 'dd/MM/yyyy')
            if (!params.CNPJ)
                params.CNPJ = null

            JasperPrint objJPrint = JasperFillManager.fillReport(objJReport, params, dataSource.connection)
            JasperViewer.viewReport(objJPrint, false);

            render view: 'index', params: params

        } catch (e) {
            e.printStackTrace()
        }



    }
}
