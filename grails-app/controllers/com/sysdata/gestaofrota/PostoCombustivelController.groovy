package com.sysdata.gestaofrota

import grails.converters.JSON


class IntervaloReembolsoCommand	{
	Integer id
	Integer parId
	Integer inicioIntervalo
	Integer fimIntervalo
	Integer diaEfetivacao
	Integer meses
}


class ReembolsoSemanalCommand {
	Integer id
	Integer parId
	DiaSemana diaSemana
	Integer intervaloDias
}


class PostoCombustivelController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def postoCombustivelService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def criteria = {
			order('id')
		}
		def postoCombustivelInstanceList = PostoCombustivel.createCriteria().list(params, criteria)
		[postoCombustivelInstanceList:postoCombustivelInstanceList , postoCombustivelInstanceTotal: PostoCombustivel.count()]
	}


    def create = {
		render(view:"form",model:[action:'novo'])    
	}

    def save = {
        def postoCombustivelInstance=new PostoCombustivel(params)
		
		postoCombustivelInstance.endereco=params['endereco']
		postoCombustivelInstance.telefone=params['telefone']
		postoCombustivelInstance.dadoBancario=params['dadoBancario']
		
        if (postoCombustivelService.save(postoCombustivelInstance)) {
			
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), postoCombustivelInstance.id])}"
            redirect(action: "show", id:postoCombustivelInstance.id)
        }
        else {
			render(view: "form", model: [postoCombustivelInstance: postoCombustivelInstance, action:'novo'])
        }
    }

    def show = {
        def postoCombustivelInstance = PostoCombustivel.get(params.long('id'))
        if (!postoCombustivelInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), params.id])}"
            redirect(action: "list")
        }
        else {
            render(view:'form',model:[postoCombustivelInstance: postoCombustivelInstance,action:'visualizando'])
        }
    }

    def edit = {
        def postoCombustivelInstance = PostoCombustivel.get(params.id)
        if (!postoCombustivelInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), params.id])}"
            redirect(action: "list")
        }
        else {
            render(view: "form", model: [postoCombustivelInstance: postoCombustivelInstance, action:'editando'])
        }
    }

    def update = {
        def postoCombustivelInstance = PostoCombustivel.get(params.id)
        if (postoCombustivelInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (postoCombustivelInstance.version > version) {
                    
                    postoCombustivelInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'postoCombustivel.label', default: 'PostoCombustivel')] as Object[], "Another user has updated this PostoCombustivel while you were editing")
                    render(view: "form", model: [postoCombustivelInstance: postoCombustivelInstance, action:'editando'])
                    return
                }
            }
            postoCombustivelInstance.properties = params
			
			postoCombustivelInstance.endereco=params['endereco']
			postoCombustivelInstance.telefone=params['telefone']
			postoCombustivelInstance.dadoBancario=params['dadoBancario']
	
			
            if (postoCombustivelService.save(postoCombustivelInstance)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), postoCombustivelInstance.id])}"
              redirect(action: "show", id:postoCombustivelInstance.id)
            }
            else {
                render(view: "form", model: [postoCombustivelInstance: postoCombustivelInstance, action:'editando'])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def postoCombustivelInstance = PostoCombustivel.get(params.id)
        if (postoCombustivelInstance) {
            try {
				log.debug('Inativando ' + postoCombustivelInstance)
				postoCombustivelInstance = PostoCombustivel.get(params.id)
				postoCombustivelInstance.status=Status.INATIVO
				postoCombustivelInstance.estabelecimentos.each {
					log.debug('Inativando ' + it)
					it.status=Status.INATIVO
				}
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				log.error("Erro ao deletar posto combustivel " + postoCombustivelInstance)
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), params.id])}"
                redirect(action: "show", params:[selectedId:params.id])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), params.id])}"
            redirect(action: "list")
        }
    }

	
	def listAllJSON={
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def offset=params.offset?:10
		def opcao
		def filtro
		def prg = Rh.get(params.prgId as Long)

		def postoCombustivelInstanceList=PostoCombustivel
				.createCriteria()
				.list(){
					//eq('status', Status.ATIVO)
					if(params.opcao && params.filtro){
						opcao=params.opcao.toInteger()
						filtro=params.filtro
						//Nome Fantasia
						if(opcao==1)
							like('nomeFantasia',filtro+'%')
						//CNPJ
						else if(opcao==2)
							like('cnpj',filtro+'%')
						//Cod Estab
						else if(opcao==3)
							estabelecimentos{like('codigo',filtro+'%')}
					}
					def userInstance=getAuthenticatedUser()
					if(userInstance.owner instanceof PostoCombustivel){
						eq('id', userInstance.owner.id)
					}
					
				}
		def postoCombustivelInstanceTotal=PostoCombustivel
				.createCriteria()
				.list(){
					eq('status', Status.ATIVO)
					if(params.opcao && params.filtro){
						//Nome Fantasia
						if(opcao==1)
							like('nomeFantasia',filtro+'%')
						//CNPJ
						else if(opcao==2)
							like('cnpj',filtro+'%')
						//Cod Estab
						else if(opcao==3)
							estabelecimentos{like('codigo',filtro+'%')}
					}
					projections{ rowCount() }
				}

		def fields=postoCombustivelInstanceList.collect{p->
			[id:p.id,
						sel: p.vinculado(prg) ? "<input type='checkbox' class='enable target' name='sel' id='sel${p.id}' checked>":"<input type='checkbox' class='enable target' name='sel' id='sel${p.id}'>",
						razao:p.nome,
						nomeFantasia:p.nomeFantasia,
						cnpj:p.cnpj,
						acao:"<a class='show' href=${createLink(action:'show')}/${p.id}></a>"]
		}

		def data=[totalRecords:postoCombustivelInstanceTotal,results:fields]
		render data as JSON
	}

	def getIntervalosReembolso={
		def postoCombustivelInstance=PostoCombustivel.get(params.id)

		def jsonData
		if(postoCombustivelInstance.tipoReembolso==TipoReembolso.INTERVALOS_MULTIPLOS){
			
			def fields=postoCombustivelInstance.reembolsos.collect{r->
			[inicio:r.inicioIntervalo,
			 fim:r.fimIntervalo,
			 diaEfetivacao:r.diaEfetivacao,
			 meses:r.meses,
			 acao:"<a href='#' onclick='openModal(${r.id});'><img src='${resource(dir:'images',file:'edit_mini.png')}' alt='Editar'></a> <a href='#' onclick='deleteReembolso(${r.id});'><img src='${resource(dir:'images',file:'delete_mini.png')}' alt='Excluir'></a>"
				 ]	
			}
			
			jsonData=[totalRecords:postoCombustivelInstance.reembolsos.size(),results:fields]
		}else
			jsonData=[totalRecords:0,results:[]]
		
		render jsonData as JSON
	}
	
	def deleteReembolso={
		def retorno
		def reembolsoInstance=Reembolso.get(params.id)
		if (reembolsoInstance) {
			try {
				reembolsoInstance.delete(flush: true)
				retorno=[type:"ok",message:"Reembolso excluído"]
			}
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				retorno=[type:"error",message:"Violação de integridade durante exclusão de Reembolso"]			
			}
		}
		render retorno as JSON
	}
	
	
	def getReembolsoSemanal={
		def postoCombustivelInstance=PostoCombustivel.get(params.id)
		
		def jsonData
		
		if(postoCombustivelInstance.tipoReembolso==TipoReembolso.SEMANAL){
			
			def fields=postoCombustivelInstance.reembolsos.collect{r->
				[diaSemana:r.diaSemana.nome,
				 intervaloDias:r.intervaloDias,
				 acao:"<a href='#' onclick='openModal(${r.id});'><img src='${resource(dir:'images',file:'edit_mini.png')}' alt='Editar'></a> <a href='#' onclick='deleteReembolso(${r.id});'><img src='${resource(dir:'images',file:'delete_mini.png')}' alt='Excluir'></a>"
					 ]
			}
			
			jsonData=[totalRecords:postoCombustivelInstance.reembolsos.size(),results:fields]
		}else
			jsonData=[totalRecords:0,results:[]]
		
		
		render jsonData as JSON
	}

	
	def manageReembolso={
		def reembolsoCommand
		
		def reembolsoInstance=Reembolso.get(params.id)
		if(reembolsoInstance){
			reembolsoCommand=new IntervaloReembolsoCommand(id:reembolsoInstance.id,
															parId:reembolsoInstance.participante.id,
															inicioIntervalo:reembolsoInstance.inicioIntervalo,
															fimIntervalo:reembolsoInstance.fimIntervalo,
															diaEfetivacao:reembolsoInstance.diaEfetivacao,
															meses:reembolsoInstance.meses)
		}else
			reembolsoCommand=new IntervaloReembolsoCommand(parId:params.parId.toInteger())
		render(template:'reembolso',model:[reembolsoInstance:reembolsoCommand])
	}
	
	
	def manageReembolsoSemanal={
		def reembolsoCommand

		def reembolsoInstance=Reembolso.get(params.id)
		if(reembolsoInstance){
			reembolsoCommand=new ReembolsoSemanalCommand(id:reembolsoInstance.id,
															parId:reembolsoInstance.participante.id,
															diaSemana:reembolsoInstance.diaSemana,
															intervaloDias:reembolsoInstance.intervaloDias)
		}else
			reembolsoCommand=new ReembolsoSemanalCommand(parId:params.parId.toInteger())
			
		render(template:'reembolsoSemanal',model:[reembolsoInstance:reembolsoCommand])
	}
	
	
	def saveReembolsoSemanal={ReembolsoSemanalCommand cmd->
		
		def postoCombustivelInstance=PostoCombustivel.get(cmd.parId)
		def reembolsoInstance=ReembolsoSemanal.get(cmd.id)
		
		def retorno
		
		if(!reembolsoInstance){
			
			if(postoCombustivelInstance.reembolsos.size()==0){
				postoCombustivelInstance.addToReembolsos(new ReembolsoSemanal(cmd.properties))
				postoCombustivelInstance.tipoReembolso=TipoReembolso.SEMANAL
				
				if(postoCombustivelInstance.save(flush:true))
					retorno=[type:"ok",message:"Reembolso inserido"]
				else{
					retorno=[type:"error",message:"Reembolso Não Inserido"]
					postoCombustivelInstance.errors.allErrors.each {
						log.error it
					}
				}
			}else{
				retorno=[type:"error",message:"Reembolso inválido! Já existe(m) reembolso(s) definido(s)"]
			}
			
		}else{
			reembolsoInstance.properties=cmd.properties
			retorno=[type:"ok",message:"Reembolso alterado"]
		}
		render retorno as JSON

	}
	
	
	def saveReembolso={IntervaloReembolsoCommand cmd->
		
		def postoCombustivelInstance=PostoCombustivel.get(cmd.parId)
		def reembolsoInstance=ReembolsoIntervalo.get(cmd.id)
		
		def inicioIntervalo=cmd.inicioIntervalo
		def fimIntervalo=cmd.fimIntervalo
		def diaEfetivacao=cmd.diaEfetivacao
		def meses=cmd.meses
		
		def valido=true
		
		def retorno
		
		def listReembolsos=postoCombustivelInstance.reembolsos
		if(!postoCombustivelInstance.tipoReembolso || postoCombustivelInstance.tipoReembolso==TipoReembolso.INTERVALOS_MULTIPLOS || listReembolsos.size()==0){
			if(inicioIntervalo<=fimIntervalo){
				
				def reembolsos=listReembolsos.findAll{reembolsoInstance?it.id!=reembolsoInstance.id:true}.sort{it.inicioIntervalo}
				reembolsos.each{r->
					if(inicioIntervalo>=r.inicioIntervalo && inicioIntervalo<=r.fimIntervalo){
						valido=false
						return
					}
				}
				if(valido){
					if(!reembolsoInstance){
						postoCombustivelInstance.addToReembolsos(new ReembolsoIntervalo(cmd.properties))
						postoCombustivelInstance.tipoReembolso=TipoReembolso.INTERVALOS_MULTIPLOS
						postoCombustivelInstance.save(flush:true)
						retorno=[type:"ok",message:"Reembolso inserido"]
					}else{
						reembolsoInstance.properties=cmd.properties
						retorno=[type:"ok",message:"Reembolso alterado"]
					}
					render retorno as JSON
				}else{
					retorno=[type:"error",message:"Intervalo inválido"]
					render retorno as JSON
				}
			}else
				retorno=[type:"error",message:"Início de intervalo superior ao Fim de Intervalo"]
		}else
			retorno=[type:"error",message:"Reembolso inválido! Já existe(m) reembolso(s) definido(s)"]
		
		render retorno as JSON
		
	}
	
}
