package br.com.acception.greport

import com.sysdata.gestaofrota.Estabelecimento

class ReportViewerController {

	def reportViewerService
	def xlsExportService
	def grailsApplication
	
    def index() { 
		redirect action:'listReports',params:params
	}
	
	
	def listReports(){
		def reportList=Report.all
		[reportList:reportList]
	}
	
	def openToParameters(){
		def reportInstance=Report.get(params.id)
		if(!reportInstance)
			flash.message="Relatório ${params.id} não encontrado na base de configuração"
		render view:"list",model:[reportInstance:reportInstance,rowCount:0] 
	}
	
	def export(){
		params.max=Math.min(params.max ? params.int('max') : 50, 100)
		
		def reportInstance=Report.get(params.id)
		
		def result=[:]
		if(reportInstance){
			params.reportInstance=reportInstance
			try {
				
				/* Trata solicitação de geração XLSX */
					
				response.contentType=grailsApplication.config.grails.mime.types[params.format]
				response.setHeader("Content-disposition","attachment;filename=${reportInstance.name}.xlsx")
				xlsExportService.export(params,response.outputStream)
					
				
			} catch (Exception e) {
				log.error e
				flash.message=e
			}
		}
		
		render view:'list',model:params+result

	}
	
	
	def list(){
		params.max=Math.min(params.max ? params.int('max') : 10, 100)
		
		def list
		def filter= Estabelecimento.enableHibernateFilter('contaFilter')
		//filter.setParameter("contaId",5)
		list= Estabelecimento.findAll()
			
		println list

		
		def reportInstance=Report.get(params.id)
		
		def result=[:]
		if(reportInstance){
		
			try {
				result=reportViewerService.list(params)
				
			} catch (Exception e) {
			
				reportInstance=Report.get(params.id)
				result=[rows:null,rowCount:0,rowTotal:0]
				
				log.error e
				flash.message=e.message
			} finally{
				params.reportInstance=reportInstance
			}
		}
		params+result
	}
}
