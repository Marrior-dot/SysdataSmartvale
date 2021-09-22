package com.sysdata.gestaofrota.relatorios.jasper

import grails.core.GrailsApplication
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.view.JasperViewer

class JasperTesteController {

    GrailsApplication grailsApplication
    def dataSource


    def index() {

        try {
            def jrxmlFileName = "src/main/groovy/com/sysdata/gestaofrota/relatorios/jasper/rel_Participantes.jrxml"
            def reportName = grailsApplication.mainContext.getResource(jrxmlFileName).file.getAbsoluteFile()
            JasperReport objJReport = JasperCompileManager.compileReport(jrxmlFileName);



            JasperPrint objJPrint = JasperFillManager.fillReport(objJReport, [:], dataSource.connection)
            JasperViewer.viewReport(objJPrint, false);

        } catch (e) {
            e.printStackTrace()
        }
    }
}
