package com.sysdata.gestaofrota

class ProcessamentoController {

    def index() {}


    def execute() {
        if (grailsApplication.mainContext.containsBean(params.id)) {
            def procDate = Util.calcularDataProc()
            try {
                def proc = grailsApplication.mainContext.getBean(params.id)
                proc.executar(procDate)
                flash.message = "Processamento ${params.id} executado"
            }
            catch (Exception e){
                e.printStackTrace()
                flash.error = e.message
            }
        } else flash.error = "Processamento $params.id não definido"
        redirect action: "index"
    }


    def executeAll() {

        def procDate = Util.calcularDataProc()

        def procList = grailsApplication.config.project.processamentos
        procList.each { p ->
            if (grailsApplication.mainContext.containsBean(p)) {
                def proc = grailsApplication.mainContext.getBean(p)
                log.info "Executando processamento $p ..."
                proc.executar(procDate)
            } else {
                log.error "Processamento $p nao definido"
                flash.message = "Processamento $p não definido"
            }
        }
        redirect action: "index"
    }
}
