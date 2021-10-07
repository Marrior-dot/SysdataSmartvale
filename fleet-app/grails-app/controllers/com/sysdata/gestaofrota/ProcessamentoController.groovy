package com.sysdata.gestaofrota

import com.fourLions.processingControl.ExecutableProcessing
import com.fourLions.processingControl.Processing
import com.fourLions.processingControl.ProcessingService
import com.sysdata.gestaofrota.proc.ReferenceDateProcessing
import grails.core.GrailsApplication

class ProcessamentoController {

    GrailsApplication grailsApplication
    ProcessingService processingService

    def index() {
        [processingList: Processing.list(sort: 'order')]
    }

    def execute(Long id) {
        Processing processing = Processing.get(id)
        if (processing) {
            Date date
            if (params.dataProcessamento)
                date = params.date('dataProcessamento', 'dd/MM/yyyy')
            else
                date = Util.calcularDataProcessamento()
            processingService.runProcessing(processing, date)
            flash.success = "Processamento (${processing.name}) executado"
        } else
            flash.error = "Processamento $params.id não definido"
        redirect action: "index"
    }

    def executeAll() {
        def processingList = Processing.list(sort: 'order')
        def data = Util.calcularDataProcessamento()
        processingList.each { p ->
            try {
                ExecutableProcessing exec = p as ExecutableProcessing
                exec.execute(data)
                flash.success = "Processamentos executados"
            } catch(e) {
                e.printStackTrace()
                flash.error = e.message
            }
        }
        redirect action: "index"
    }
}
