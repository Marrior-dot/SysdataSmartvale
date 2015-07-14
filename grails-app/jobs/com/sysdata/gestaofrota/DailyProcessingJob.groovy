package com.sysdata.gestaofrota

class DailyProcessingJob {
	
    static triggers = {
		cron name:'dailyProcessing', cronExpression:"00 30 01 ? * *" 
    }
	
	def transacaoService
	
    def execute() {
		
		log.info "Sysdata - Amazon Card - Gestão de Frota - 2015"
		log.info "Iniciando Processamento Diário..."
		
        transacaoService.agendarAll()
		
		log.info "Processamento Diário finalizado"
    }
}
