package com.sysdata.gestaofrota


import grails.converters.JSON
import grails.orm.PagedResultList

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
        Integer offset = params.int('offset', 0)
        Integer max = params.int('max', 100)
        PagedResultList arquivoList = Arquivo.createCriteria().list(max: max, offset: offset){
            order('dateCreated','desc')
        }

        def data=[totalRecords:arquivoList.getTotalCount(), results:arquivoList as List<Arquivo>]
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
		println "params ${params}"
		redirect(action: "list", params: params)
	}
	
	def downloadFile() {
		Arquivo arquivoInstance=Arquivo.get(params.long('id'))
		String conteudo = new String(arquivoInstance.conteudoText)
		if(conteudo){
			response.setContentLength(conteudo.size())
			response.setHeader("Content-Disposition","attachment;filename=${arquivoInstance.nome}.txt")
			println "conteudo: ${conteudo}"
			response.outputStream<<conteudo
		}else
			response.sendError(404)
	}
}
