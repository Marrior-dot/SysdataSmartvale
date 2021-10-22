package com.sysdata.gestaofrota.relatorios.jasper

import grails.core.GrailsApplication
import grails.util.Environment
import com.sysdata.gestaofrota.Util
import grails.core.GrailsApplication
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.view.JasperViewer

class JasperBaseRelatorioController {

    def dataSource

    def exportToPdf(grailsApp, reportName, pars, outputStream) {
        def reportFilename = "${grailsApp.config.projeto.relatorios.jasperDiretorio}/${reportName}.jrxml"
        if (!new File(reportFilename).exists())
            throw new RuntimeException("Arquivo de relatório '${reportFilename}' não encontrado!")

        JasperReport objJReport = JasperCompileManager.compileReport(reportFilename)
        JasperPrint jasperPrint = JasperFillManager.fillReport(objJReport, pars, dataSource.connection)
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream)
    }
}
