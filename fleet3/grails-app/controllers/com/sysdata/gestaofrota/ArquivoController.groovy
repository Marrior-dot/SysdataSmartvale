package com.sysdata.gestaofrota


import grails.converters.JSON
import grails.orm.PagedResultList

import java.text.SimpleDateFormat


class ArquivoController {

	
	def geracaoArquivoEmbossingService

	def list={}

	def newList={

	}
	
	def listAllJSON() {
        Integer offset = params.int('offset', 0)
        Integer max = params.int('max', 100)
        PagedResultList arquivoList = Arquivo.createCriteria().list(max: max, offset: offset){
            order('dateCreated','desc')
        }

		def arqMap = arquivoList.collect {
			[
				data: it.dateCreated.format('dd/MM/yy HH:mm'),
				tipo: it.tipo.name,
				nome: it.nome,
				status: it.status.name,
				acao: """
&nbsp;&nbsp&nbsp<a href='${g.createLink(action:'downloadFile', id: it.id)}'><span class='glyphicon glyphicon-download'/></a>&nbsp;&nbsp&nbsp;
<a href='${g.createLink(action:'delete', id: it.id)}'><span class='glyphicon glyphicon-remove'/></a>
"""
			]
		}

        def data=[totalRecords:arquivoList.getTotalCount(), results: arqMap]
		render data as JSON
	}

	def generateEmbossing() {
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
	
	def downloadFile() {
		Arquivo arquivoInstance=Arquivo.get(params.long('id'))
		String conteudo = new String(arquivoInstance.conteudoText)
		if(conteudo){
			response.setContentLength(conteudo.size())
			response.setHeader("Content-Disposition","attachment;filename=${arquivoInstance.nome}.txt")
			response.outputStream<<conteudo
		}else
			response.sendError(404)
	}

	def delete() {
		Arquivo arquivoInstance = Arquivo.get(params.long('id'))
		try {
			arquivoInstance.delete(flush: true)
			flash.message = "Arquivo #${params.id} removido"
		} catch (e) {
			flash.message = "Erro: $e"
		}
		redirect(action: "list", params: params)
	}
}
