package com.sysdata.gestaofrota

import grails.gorm.DetachedCriteria

import java.text.SimpleDateFormat


class RelatorioController {

	def exportService
	
    def index = { }
	
	
	private def parseDate(dateStr,lastDate=false){
		
		def df=new SimpleDateFormat("dd/MM/yyyy")
		def parsedDate=df.parse(dateStr)
		
		if(lastDate){
			def cal=Calendar.getInstance()
			cal.setTime(parsedDate)
			cal.add(Calendar.DAY_OF_MONTH,1)
			parsedDate=cal.getTime()
		}
			
		parsedDate
	}
	
	
	
	def reportProjecaoReembolso(){
		
		def beginDate=parseDate(params.dataInicio)
		def endDate=parseDate(params.dataFim,true)
		
		def criteria=new DetachedCriteria(Lancamento).build{
			eq "tipo",TipoLancamento.REEMBOLSO
			eq "status",StatusLancamento.A_EFETIVAR
			if(beginDate && endDate)
				between "dataEfetivacao",beginDate,endDate
		}
		
		
		
		
	}
	
	
	def list={
		def dataInicio
		def dataFim
		
		def df=new SimpleDateFormat("dd/MM/yyyy")
		
		if(params.dataInicio && params.dataFim){
			dataInicio=df.parse(params.dataInicio)
			dataFim=df.parse(params.dataFim)
			def cal=Calendar.getInstance()
			cal.setTime(dataFim)
			cal.add(Calendar.DAY_OF_MONTH,1)
			dataFim=cal.getTime()
		}
		
		def transacaoInstanceTotal=Abastecimento.withCriteria{
			eq("autorizada",Boolean.TRUE)
			if(dataInicio && dataFim)
				between('dateCreated', dataInicio, dataFim)
		}.size()

		
		def transacaoList=Abastecimento.withCriteria{
			eq("autorizada",Boolean.TRUE)
			if(dataInicio && dataFim)
				between('dateCreated', dataInicio, dataFim)
		}
		
		def kmAnt
		def transacaoInstanceList=[]
		
		transacaoList.each{tr->
			
			transacaoInstanceList<<
			[id:tr.id,
				datahora:Util.formattedDateTime(tr.dateCreated),
				codestab:tr.codigoEstabelecimento,
				condutor:tr.participante.nome,
				placa:tr.placa,
				hodometro:tr.quilometragem,
				combustivel:tr.combustivel,
				litros:Util.formatCurrency(tr.valor/tr.precoUnitario),
				preco:Util.formatCurrency(tr.precoUnitario),
				total:Util.formatCurrency(tr.valor),
				percorrido:kmAnt?tr.quilometragem-kmAnt:0,
				status:tr.status.nome]
			kmAnt=tr.quilometragem
		}
		
		if(params?.format && params.format != "html"){
			response.contentType = ConfigurationHolder.config.grails.mime.types[params.format]
			response.setHeader("Content-disposition", "attachment; filename=transacoes.${params.extension}")

			List fields=["id","datahora","codestab","condutor","placa","hodometro","combustivel","litros","preco","total","percorrido","status"]
			Map labels=["id":"ID","datahora":"Data/Hora","codestab":"Cod Estab","condutor":"Condutor","placa":"Placa",
						"hodometro":"Hodômetro","combustivel":"Combustível","litros":"Litros","preco":"Preço Unit","total":"Total",
						"percorrido":"Percorrido(km)","status":"Status"]

			exportService.export(params.format, response.outputStream,transacaoInstanceList,fields,labels, [:], [:])
		}


		def pars=["dataInicio":dataInicio,"dataFim":dataFim]
		
		[transacaoInstanceList:transacaoInstanceList,transacaoInstanceTotal:transacaoInstanceTotal,pars:pars]
	}
}
