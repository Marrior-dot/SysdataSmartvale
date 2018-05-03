package com.sysdata.gestaofrota

class DailyProcessingJob {
	
    static triggers = {
		cron name:'dailyProcessing', cronExpression:"00 30 01 ? * *" 
    }
	
	def transacaoService

    def grailsApplication
	
    def execute() {

        def nomeProj=grailsApplication.config.project.nome

        def dataProc=Util.calcularDataProc()
		
		log.info "Sysdata - Gestao de Frota - ${nomeProj}"

		log.info "Iniciando Processamento Diário (${dataProc.format('dd/MM/yyyy')})..."

        grailsApplication.config.processamentos.each{pr->

            def proc=grailsApplication.mainContext.getBean(pr)
            proc.execute()
        }


        //transacaoService.agendarAll()
		
		log.info "Processamento Diário finalizado"
    }
}
