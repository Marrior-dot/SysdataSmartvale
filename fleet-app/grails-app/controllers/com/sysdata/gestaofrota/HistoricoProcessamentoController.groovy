package com.sysdata.gestaofrota

import com.fourLions.processingControl.ProcessingExecution

class HistoricoProcessamentoController {

    def index() {
        params.max = params.max ? params.max : 10
        params.sort = "startTime"
        params.order = "desc"
        [
            historicoList: ProcessingExecution.list(params),
            historicoCount: ProcessingExecution.count()
        ]
    }

    def show() {
        [historicoProcessamento: ProcessingExecution.get(params.id.toLong())]
    }
}
