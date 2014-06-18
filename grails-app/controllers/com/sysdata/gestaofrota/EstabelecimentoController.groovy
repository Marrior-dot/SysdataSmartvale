package com.sysdata.gestaofrota

import grails.converters.JSON

class EstabelecimentoController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def estabelecimentoService
	

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [estabelecimentoInstanceList: Estabelecimento.list(params), estabelecimentoInstanceTotal: Estabelecimento.count()]
    }

    def create = {
		if(params.empId){
			def empresaInstance=PostoCombustivel.get(params.empId)
			render(view:"form",model:[empresaInstance:empresaInstance,action:Util.ACTION_NEW])
		}else{
			flash.message="Empresa não selecionada!"
			redirect(action:'list')
		}
    }

    def save = {
		flash.errors=[]
		def estabelecimentoInstance = new Estabelecimento(params)
		estabelecimentoInstance.endereco=params['endereco']
		estabelecimentoInstance.telefone=params['telefone']
		
		try {
			Estabelecimento.withTransaction{
				if(params.empId){
					def empresaInstance=PostoCombustivel.get(params.empId)
					estabelecimentoInstance.empresa=empresaInstance
					if (estabelecimentoService.gerarCodigo(estabelecimentoInstance) && estabelecimentoService.save(estabelecimentoInstance)) {
						flash.message = "${message(code: 'default.created.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), estabelecimentoInstance.nome])}"
						redirect(action: "show", id:estabelecimentoInstance.id)
					}
					else {
						render(view: "form", model: [estabelecimentoInstance: estabelecimentoInstance, empresaInstance:estabelecimentoInstance.empresa, action:Util.ACTION_NEW])
					}
				}else{
					flash.message="Estabelecimento não relacionado a uma Empresa específica"
					render(view: "form", model: [estabelecimentoInstance: estabelecimentoInstance,empresaInstance:estabelecimentoInstance.empresa,action:Util.ACTION_NEW])
				}
			}
	
		} catch (Exception e) {
			flash.errors=e.message
			render(view: "form", model: [estabelecimentoInstance: estabelecimentoInstance, empresaInstance:estabelecimentoInstance.empresa, action:Util.ACTION_NEW])
		}
    }

    def show = {
		def estabelecimentoInstance = Estabelecimento.get(params.id)
		if (!estabelecimentoInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), params.id])}"
			redirect(action: "list")
		}
		else {
			render(view:'form',model:[estabelecimentoInstance: estabelecimentoInstance,empresaInstance:estabelecimentoInstance.empresa,action:Util.ACTION_VIEW])
		}
    }

    def edit = {
        def estabelecimentoInstance = Estabelecimento.get(params.id)
        if (!estabelecimentoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), params.id])}"
            redirect(action: "list")
        }
        else {
            render view:'form',model:[estabelecimentoInstance: estabelecimentoInstance,action:'editando']
        }
    }

    def update = {
        def estabelecimentoInstance = Estabelecimento.get(params.id)
        if (estabelecimentoInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (estabelecimentoInstance.version > version) {
                    
                    estabelecimentoInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'estabelecimento.label', default: 'Estabelecimento')] as Object[], "Another user has updated this Estabelecimento while you were editing")
                    render(view: "edit", model: [estabelecimentoInstance: estabelecimentoInstance])
                    return
                }
            }
            estabelecimentoInstance.properties = params
			
			estabelecimentoInstance.endereco=params['endereco']
			estabelecimentoInstance.telefone=params['telefone']
	
            if (estabelecimentoService.save(estabelecimentoInstance)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), estabelecimentoInstance.id])}"
                redirect(action: "show", id: estabelecimentoInstance.id)
            }
            else {
                render(view: "edit", model: [estabelecimentoInstance: estabelecimentoInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def estabelecimentoInstance = Estabelecimento.get(params.id)
        if (estabelecimentoInstance) {
            try {
                estabelecimentoInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'estabelecimento.label', default: 'Estabelecimento'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def listAllJSON={
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def offset=params.offset?:10
		def empId=params.empId!='null'?params.empId.toLong():null

		def estabelecimentoInstanceList=Estabelecimento
											.createCriteria()
											.list(max:params.max,offset:offset){
													empresa{eq('id',empId)}
											}
		def estabelecimentoInstanceTotal=Estabelecimento
												.createCriteria()
												.list(){
													empresa{eq('id',empId)}
													projections{ rowCount() }
												}

		def fields=estabelecimentoInstanceList.collect{e->
			[id:e.id,
						razao:e.nome,
						nomeFantasia:e.nomeFantasia,
						codEstab:e.codigo,
						acao:"""<a href="${createLink(controller:'estabelecimento',action:'show')}/${e.id}" class='show'></a>"""]
		}

		def data=[totalRecords:estabelecimentoInstanceTotal,results:fields]
		render data as JSON
	}
	
	def getByCodigo={
		def estabelecimentoInstance=Estabelecimento.findByCodigo(params.codigo)
		def data=[razao:estabelecimentoInstance?.empresa?.nome]
		render data as JSON
	}
	
}