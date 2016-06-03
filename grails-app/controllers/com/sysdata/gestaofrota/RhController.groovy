package com.sysdata.gestaofrota

import grails.converters.JSON

import grails.plugins.springsecurity.Secured



class RhController extends BaseOwnerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def participanteService
	def rhService

	@Secured(['IS_AUTHENTICATED_FULLY'])
    def index = {
        redirect(action: "list", params: params)
    }

	@Secured(['IS_AUTHENTICATED_FULLY'])
    def list = {   }

	@Secured(['IS_AUTHENTICATED_FULLY'])
	def newList = {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def criteria = {
			order('id', 'desc')
		}
		def rhInstanceList = Rh.createCriteria().list(params, criteria)
		/*[rhInstanceList:rhInstanceList , rhInstanceTotal: Rh.count()]*/
	}

	@Secured(['ROLE_PROC','ROLE_ADMIN'])
    def create = {
		render(view:'form',model:[action:Util.ACTION_NEW])
    }

	@Secured(['ROLE_PROC','ROLE_ADMIN'])
    def save = {
        def rhInstance=new Rh(params)
		
		rhInstance.endereco=params['endereco']
		rhInstance.telefone=params['telefone']
		
        if (rhService.saveNew(rhInstance)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'rh.label', default: 'Rh'), rhInstance.id])}"
            redirect(action: "show", id: rhInstance.id)
        }
        else {
            render(view: "form", model: [rhInstance: rhInstance,action:Util.ACTION_NEW])
        }
    }

	@Secured(['IS_AUTHENTICATED_FULLY'])
    def show = {
        def rhInstance = Rh.get(params.id)
        if (!rhInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'rh.label', default: 'Rh'), params.rhId])}"
            redirect(action: "list")
        }
        else {
			clearSession()
            render(view:'form',model:[rhInstance:rhInstance,action:Util.ACTION_VIEW])
        }
    }

	@Secured(['ROLE_PROC','ROLE_ADMIN'])
    def edit = {
        def rhInstance = Rh.get(params.id)
        if (!rhInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'rh.label', default: 'Rh'), params.id])}"
            redirect(action: "list")
        }
        else {
            return render(view:'form',model:[rhInstance:rhInstance,action:Util.ACTION_EDIT])
        }
    }

	@Secured(['ROLE_PROC','ROLE_ADMIN'])
    def update = {
        def rhInstance = Rh.get(params.id)
        if (rhInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (rhInstance.version > version) {
                    
                    rhInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'rh.label', default: 'Rh')] as Object[], "Another user has updated this Rh while you were editing")
                    render(view:'form',model:[rhInstance:rhInstance,action:Util.ACTION_EDIT])
                    return
                }
            }
			
			
			
            rhInstance.properties = params
			rhInstance.endereco=params['endereco']
			rhInstance.telefone=params['telefone']
			
			participanteService.saveCidade(rhInstance.endereco)
            if (!rhInstance.hasErrors() && rhInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'rh.label', default: 'Rh'), rhInstance.id])}"
                redirect(action: "show", id:rhInstance.id)
            }
            else {
                render(view:'form',model:[rhInstance:rhInstance,action:Util.ACTION_EDIT])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'rh.label', default: 'Rh'), params.id])}"
            redirect(action: "list")
        }
    }

	@Secured(['ROLE_PROC','ROLE_ADMIN'])
    def delete = {
		def rhInstance = Rh.get(params.id)
		if (rhInstance) {
			try {
				log.debug("Inativando " + rhInstance)
				rhInstance.status = Status.INATIVO
				rhInstance.empresas.each { empresa ->
					log.debug("Inativando " + empresa)
					empresa.status = Status.INATIVO
					empresa.estabelecimentos.each { estabelecimento ->
						log.debug("Inativando " + estabelecimento)
						estabelecimento.status = Status.INATIVO
					}
				}
				rhInstance.unidades.each { uni ->
					log.debug("Inativando " + uni)
					uni.status = Status.INATIVO
					uni.funcionarios.each { fun ->
						log.debug("Inativando " + fun)
						fun.status = Status.INATIVO
					}
				}
				flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'rh.label', default: 'Rh'), params.id])}"
				redirect(action: "list")
			}
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'rh.label', default: 'Rh'), params.id])}"
				redirect(action: "show", params:[rhId: rhInstance.id])
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'rh.label', default: 'Rh'), params.id])}"
			redirect(action: "list")
		}
    }
	
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def autoCompleteJSON={
		
		def list
		withSecurity{ownerList->
			
			list = Rh.withCriteria{
							eq('status', Status.ATIVO)
							if(ownerList.size>0)
								'in'('id',ownerList)
				
							if(params.query)
								like("nomeFantasia",params.query+"%")
						}

		}
		
				
		def jsonList = list.collect { [ id: it.id, name: it.nomeFantasia ] }
		def jsonResult = [
			result: jsonList
		]
		render jsonResult as JSON
	}
	
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def listAllJSON={
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def offset=params.offset?:0
		def opcao
		def filtro

		def rhInstanceList
		
		withSecurity{ownerList->
			rhInstanceList=Rh
							.createCriteria()
							.list(max:params.max,offset:offset){
								eq('status', Status.ATIVO)
								if(ownerList.size>0)
									'in'('id',ownerList)
								
								if(params.opcao && params.filtro){
									opcao=params.opcao.toInteger()
									filtro=params.filtro
									//Código
									if(opcao==1)
										eq('codigo',filtro)
									//Nome Fantasia
									else if(opcao==2)
										like('nomeFantasia',filtro+'%')
									//CNPJ
									else if(opcao==3)
										like('cnpj',filtro+'%')
								}
							}

		}
		
		def rhInstanceTotal
		
		withSecurity{ownerList->
			rhInstanceTotal=Rh
						.createCriteria()
						.list(){
							eq('status', Status.ATIVO)
							if(params.opcao && params.filtro){
								
								if(ownerList.size>0)
									'in'('id',ownerList)

								//Código
								if(opcao==1)
									eq('codigo',filtro)
								//Nome Fantasia
								else if(opcao==2)
									like('nomeFantasia',filtro+'%')
								//CNPJ
								else if(opcao==3)
									like('cnpj',filtro+'%')
							}
							projections{ rowCount() }
						}
		}
		
		def fields=rhInstanceList.collect{r->
			[id:r.id,
				codigo:r.codigo,		
				razao:r.nome,
				fantasia:r.nomeFantasia,
				cnpj:r.cnpj,
				acao:"<a class='show' href=${createLink(action:'show')}/${r.id}></a>"]
		}

		def data=[totalRecords:rhInstanceTotal,results:fields]
		render data as JSON
	}
	
	
	private void clearSession(){
		if(session.mEstIds){
			session.mEstIds=null
			println "Limpou Est IDS da HTTP Session"
		}
	}
	
	
	/* Preenche map de estabelecimentos selecionados na HTTP Session conforme demanda 
	 */
	private void fillEstMapOnDemand(oid,chk){
		def mIds=session.mEstIds
		if(!mIds) mIds=[:]

		def eid=mIds.find{k,v->k==oid}
		if(eid) eid.value=chk
		else mIds[oid]=chk
		
		if(mIds) session.mEstIds=mIds
	
	}
	
	
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def listEmpresasJSON(){
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def offset=params.offset?params.int('offset'):0
		
		def progInstance=Rh.get(params.prgId)
		
		def empresaList=PostoCombustivel.findAllByStatus(Status.ATIVO, [max: params.max, offset: offset])

		def resultList=[]
				
		empresaList.each {e->
			def check=progInstance.empresas.find{it.id==e.id}!=null
			resultList<<[id:e.id,fantasia:e.nomeFantasia,razao:e.nome,selecao:check]
			fillEstMapOnDemand(e.id,check)
			
		}
		
		
		def jsonList=[totalRecords:PostoCombustivel.count(),results:resultList]
		
		response.setHeader("Cache-control","no-store")
		
		render jsonList as JSON
	}

	@Secured(['IS_AUTHENTICATED_FULLY'])
	def syncOne(){
		def oid=params.oid as long
		def chk=params.chk
		
		fillEstMapOnDemand(oid,chk)
		
		render "ok"
	}
	
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def syncAll(){
		session.selAllEst=params.chk?:null
		render "ok"
		
	}
	
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def saveEstabs(){
		def progInstance=Rh.get(params.prgId)
		if(progInstance){
			
			/* Marca todas as empresas no HTTP Session */
			if(session.selAllEst){
				
				PostoCombustivel.all.each{
					fillEstMapOnDemand(it.id,true)
				}
				
				session.selAllEst=null
			}
			
			
			def mIds=session.mEstIds
			
			if(mIds){
				mIds.each{k,v->
					def estInstance=progInstance.empresas.find{it.id==k}
					
					if(estInstance){
						if(v=="false"){
							progInstance.removeFromEmpresas estInstance
							println "Removeu Empresa ${estInstance?.id} da relação"
						}
							
					}else{
						estInstance=PostoCombustivel.get(k)
						if(v=="true")
							progInstance.addToEmpresas estInstance
					}
				}
					
				if(progInstance.save(flush:true)){
					render "ok"
				}else{
					def data=[type:"error",message:"Erro ao Salvar Programa"]
					render data as JSON
				}
			}else{
				def data=[type:"error",message:"Não há dados para salvar"]
				render data as JSON
			}
		}
	}
	
	
}
