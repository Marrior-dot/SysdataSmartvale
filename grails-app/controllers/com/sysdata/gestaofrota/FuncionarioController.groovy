package com.sysdata.gestaofrota

import grails.converters.JSON
import grails.plugins.springsecurity.Secured

import java.text.SimpleDateFormat


import com.sysdata.gestaofrota.exception.FuncionarioException

@Secured(['IS_AUTHENTICATED_FULLY'])
class FuncionarioController extends BaseOwnerController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def funcionarioService

	def index = {
		redirect(action: "list", params: params)
	}

	def list = {}

	def selectRhUnidade={
		render(view:'/selectRhUnidade',model:[controller:"funcionario",action:Util.ACTION_NEW]) 
	}

	def create = {
		if(params.unidade_id){
			def unidadeInstance=Unidade.get(params.unidade_id)
			render(view:"form",model:[unidadeInstance:unidadeInstance,action:Util.ACTION_NEW])
		}else{
			flash.message="Unidade não selecionada!"
			redirect(action:'list') 
		}
	}

	def save = {
		flash.errors=[]
		
		def funcionarioInstance = new Funcionario(params)
		Funcionario.withTransaction{status->
			try {
				if(params.unidId){
					def unidadeInstance=Unidade.get(params.unidId)
					funcionarioInstance.unidade=unidadeInstance
					
					funcionarioInstance.endereco=params['endereco']
					funcionarioInstance.telefone=params['telefone']
					
					if (funcionarioService.save(funcionarioInstance) && funcionarioService.gerarCartao(funcionarioInstance)) {
					flash.message = "${message(code: 'default.created.message', args: [message(code: 'funcionario.label', default: 'Funcionaroi'), funcionarioInstance.id])}"
						redirect(action: "show", id:funcionarioInstance.id)
						return 
					}
					else {
						render(view: "form", model: [funcionarioInstance: funcionarioInstance, unidadeInstance:funcionarioInstance.unidade, action:Util.ACTION_NEW])
					}
				}else{
					flash.message="Funcionário não relacionado a uma Unidade específica"
					render(view: "form", model: [funcionarioInstance: funcionarioInstance,unidadeInstance:funcionarioInstance.unidade,action:Util.ACTION_NEW])
				}
			} catch (FuncionarioException e) {
				log.error e
				status.setRollbackOnly()
				flash.errors<<e.message
				render(view: "form", model: [funcionarioInstance: funcionarioInstance, unidadeInstance:funcionarioInstance.unidade, action:Util.ACTION_NEW])
			}
		}
	
	}

	def show = {
		def funcionarioInstance = Funcionario.get(params.id)
		if (!funcionarioInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
			redirect(action: "list")
		}
		else {
			render(view:'form',model:[funcionarioInstance: funcionarioInstance,unidadeInstance:funcionarioInstance.unidade,action:Util.ACTION_VIEW])
		}
	}

	def edit = {
		def funcionarioInstance = Funcionario.get(params.id)
		if (!funcionarioInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
			redirect(action: "list")
		}
		else {
			render(view:'form',model:[funcionarioInstance:funcionarioInstance,unidadeInstance:funcionarioInstance.unidade,action:Util.ACTION_EDIT])
		}
	}

	def update = {
		def funcionarioInstance = Funcionario.get(params.id)
		if (funcionarioInstance) {
			if (params.version) {
				def version = params.version.toLong()
				if (funcionarioInstance.version > version) {

					funcionarioInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [
						message(code: 'funcionario.label', default: 'Funcionario')]
					as Object[], "Another user has updated this Funcionario while you were editing")
					render(view:'form',model:[funcionarioInstance:funcionarioInstance,unidadeInstance:funcionarioInstance.unidade,action:Util.ACTION_EDIT])
					return
				}
			}
			
			Funcionario.withTransaction{status->
				try{
					
					funcionarioInstance.properties = params
					
					funcionarioInstance.endereco=params['endereco']
					funcionarioInstance.telefone=params['telefone']

					if (funcionarioService.save(funcionarioInstance)) {
						flash.message = "${message(code: 'default.updated.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), funcionarioInstance.id])}"
						redirect(action: "show", id:funcionarioInstance.id)
					}
					else {
						render(view:'form',model:[funcionarioInstance:funcionarioInstance,unidadeInstance:funcionarioInstance.unidade,action:Util.ACTION_EDIT])
					}
				}catch(FuncionarioException e){
					status.setRollbackOnly()
					log.error e
				}
			}

		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
			redirect(action: "list")
		}
	}

	def delete = {
		def funcionarioInstance = Funcionario.get(params.id)
		if (funcionarioInstance) {
			try {
				log.debug("Inativando " + funcionarioInstance)
				funcionarioInstance.status = Status.INATIVO
				flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
				redirect(action: "list")
			}
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
				redirect(action: "show", params:[funcId:funcionarioInstance.id])
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
			redirect(action: "list")
		}
	}


	def listAllJSON={
		
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def offset=params.offset?:0
		def opcao
		def filtro
		def unidId=params.unidade_id?(params.unidade_id!='null'?params.unidade_id.toLong():null):null
		def categId=params.categId
		def gestor=params.gestor
		
		def funcionarioInstanceList
		
		withSecurity{ownerList->
			funcionarioInstanceList=Funcionario
									.createCriteria()
									.list(max:params.max,offset:offset){
										eq('status', Status.ATIVO)
										if(ownerList.size>0)
											unidade{rh{'in'('id',ownerList)}}
										
										if(unidId)
											unidade{eq('id',unidId)}
											
										if(categId)
											categoria{eq('id',categId)}
											
										if(gestor && gestor!="null")
											eq("gestor",true)
											
										if(params.opcao && params.filtro){
											opcao=params.opcao.toInteger()
											filtro=params.filtro
											//Matricula
											if(opcao==1)
												like('matricula',filtro+'%')
											//Nome
											else if(opcao==2)
												like('nome',filtro+'%')
											//CPF
											else if(opcao==3)
												like('cpf',filtro+'%')
										}
									}
		}
		
		def funcionarioInstanceTotal
		
		withSecurity{ownerList->
			funcionarioInstanceTotal=Funcionario
										.createCriteria()
										.list(){
											eq('status', Status.ATIVO)
											if(ownerList.size>0)
												unidade{rh{'in'('id',ownerList)}}
											
											if(unidId)
												unidade{eq('id',unidId)}
												
											if(categId)
												categoria{eq('id',categId)}
												
											if(gestor!="null")
												eq("gestor",true)
	
											if(params.opcao && params.filtro){
												//Matricula
												if(opcao==1)
													like('matricula',filtro+'%')
												//Nome
												else if(opcao==2)
													like('nome',filtro+'%')
												//CPF
												else if(opcao==3)
													like('cpf',filtro+'%')
											}
											projections{ rowCount() }
										}
		}

		def fields=funcionarioInstanceList.collect{f->
			[id:f.id,
						matricula:f.matricula,
						nome:f.nome,
						cpf:f.cpf,
						acao:"<a class='show' href=${createLink(action:'show')}/${f.id}></a>"]
		}

		def data=[totalRecords:funcionarioInstanceTotal,results:fields]
		render data as JSON
	}

	def beforeInterceptor=[
		action:{
			def df=new SimpleDateFormat("dd/MM/yyyy")
			params.dataNascimento=(params.dataNascimento)?df.parse(params.dataNascimento):null
			params.validadeCnh=(params.validadeCnh)?df.parse(params.validadeCnh):null
		},
		only:['save','update']
	]
	
	def search={
		//render(template:"search",model:[controller:params.controller,unidade_id:params.unidade_id,action:params.action])
		render template:"search",model:params
	}
	
	def getByMatricula={
		def funcionarioInstance=Funcionario.findByMatricula(params.matricula)
		render "${funcionarioInstance?.nome}"
	}
	
	
	def generateNewPassword(){
		def ret=[:]
		
		def cartaoInstance=Cartao.get(params.id)
		
		if(cartaoInstance){
			def newPsw=funcionarioService.gerarSenha()
			cartaoInstance.senha=newPsw
			cartaoInstance.save(flush:true)
		}
		ret['newPsw']=cartaoInstance.senha
		render ret as JSON
	}
	
}
