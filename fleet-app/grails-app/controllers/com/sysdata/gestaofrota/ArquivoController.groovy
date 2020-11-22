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
        def arquivoList = Arquivo.list()

		def arqMap = arquivoList.collect {
			[
				id: it.id,
				data: it.dateCreated.format('dd/MM/yy HH:mm'),
				tipo: it.tipo.name,
				nome: it.nome,
				status: it.status.name,
				acao: """
&nbsp;&nbsp&nbsp<a href='${g.createLink(action:'downloadFile', id: it.id)}'><span class='glyphicon glyphicon-download'/></a>&nbsp;&nbsp&nbsp;
<a href='${g.createLink(action:'delete', id: it.id)}'><span class='glyphicon glyphicon-remove'/></a>&nbsp;&nbsp&nbsp;
<a href='${g.createLink(action:'regerarArquivo', id: it.id)}'><span class='glyphicon glyphicon-glyphicon glyphicon-repeat'/>
"""
			]
		}
        def data = [totalRecords: Arquivo.count(), results: arqMap]
		render data as JSON
	}

	def generateEmbossing() {
		try {
			if(geracaoArquivoEmbossingService.gerarArquivo())
				flash.message = "Arquivo de Embossing gerado"
			else
				flash.message="Nao há cartões a embossar"
		} catch (Exception e) {
			log.error(e.message)
			e.printStackTrace()
			flash.message = "Erro ao gerar Arquivo de Embossing"
		}
		redirect(action: "list", params: params)
	}

	def regerarArquivo() {
		if (params.id) {
			Arquivo arquivo = Arquivo.get(params.long('id'))
			if (arquivo) {
				def ret = geracaoArquivoEmbossingService.regerarArquivo(arquivo)
				flash.message = ret.message
			} else {
				flash.message = "Arquivo com ID: $params.id não encontrado"
			}
		} else {
			flash.message = "Parâmetro ID não enviado na request"
		}
		redirect(action: "list")
	}
	
	def downloadFile() {
		Arquivo arquivoInstance=Arquivo.get(params.long('id'))
		String conteudo = new String(arquivoInstance.conteudoText)
		if(conteudo){
			response.setContentLength(conteudo.size())
			response.setHeader("Content-Disposition","attachment;filename=${arquivoInstance.nome}")
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
