package com.sysdata.gestaofrota


import grails.converters.JSON
import java.text.SimpleDateFormat


class ArquivoController {

	
	def geracaoArquivoEmbossingService

	def beforeInterceptor=[
		action:{
			def df=new SimpleDateFormat("dd/MM/yyyy")
			params.dataInicio=(params.dataInicio)?df.parse(params.dataInicio):null
			params.dataFim=(params.dataFim)?df.parse(params.dataFim):null
		},
		only:['listAllJSON']
	]
	
	
	def list={}

	def newList={

	}
	
	def listAllJSON={
				
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def offset=params.offset?params.offset as int:0
		
		def dataInicio=params.dataInicio
		def dataFim=params.dataFim

		def tipoArquivo=params.tipoArquivo
		def arquivoList
		def arquivoCount=0

			arquivoList=Arquivo.withCriteria(){
				if(dataInicio && dataFim)
					between('dateCreated',dataInicio,dataFim+1)
			}
			
			arquivoCount=Arquivo.withCriteria(){

				if(dataInicio && dataFim)
					between('dateCreated',dataInicio,dataFim+1)
				projections{rowCount()}
			}

		
		def fields=arquivoList.collect{a->
			[	id:a.id,
				date:new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(a.dateCreated),
				tipo:a.tipo.name,
				status:a.status.name,
				nome:a.nome,
				acao:"""<a href="${createLink(action:'downloadFile',id:a.id)}" >Download</a>"""
			]
		}
		def data=[totalRecords:arquivoCount,results:fields]
		render data as JSON
	}

	def generateEmbossing={
		try {
			if(geracaoArquivoEmbossingService.gerarArquivo())
				flash.message="Arquivo de Embossing gerado"
			else
				flash.message="Nao há cartões a embossar"
		} catch (Exception e) {
			flash.message="Erro ao gerar Arquivo de Embossing"
			log.error e
		}
		redirect(action: "list", params: params)
	}
	
	def downloadFile={
		def arquivoInstance=Arquivo.get(params.id)
		if(arquivoInstance?.conteudo){
			response.setContentLength(arquivoInstance.conteudo.size())
			response.setHeader("Content-Disposition","attachment;filename=${arquivoInstance.nome}")
			response.outputStream<<arquivoInstance.conteudo
		}else
			response.sendError(404)
	}
}
