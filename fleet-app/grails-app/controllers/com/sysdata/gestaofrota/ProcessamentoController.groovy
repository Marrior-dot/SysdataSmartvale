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
        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0
        params.sort = "order"
        [processingList: Processing.list(params), processingCount: Processing.count()]
    }

    def execute(Long id) {
        Processing processing = Processing.get(id)
        if (processing) {
            Date date = Util.calcularDataProcessamento()
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
