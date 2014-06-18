package com.sysdata.gestaofrota

import grails.gorm.DetachedCriteria
import grails.plugins.springsecurity.Secured

import com.sysdata.gestaofrota.exception.TransacaoException


class TransacaoCommand{
	String codigoEstabelecimento
	String placa
	String numeroCartao
	Double valor
	String senha
	Veiculo veiculo
	Cartao cartao
	Estabelecimento estabelecimento
	String combustivel
	Long quilometragem
	Double precoUnitario
	Long ultimaQuilometragem
}

@Secured(["IS_AUTHENTICATED_FULLY"])
class TransacaoController extends BaseOwnerController{

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def transacaoService,autorizadorService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max=Math.min(params.max ? params.int('max') : 10, 100)
		
		def transacaoInstanceList
		def transacaoInstanceTotal 
		
		withSecurity{ownerList->
			
			def pars=[:]
			pars.max=params.max
			pars.offset=params.offset?params.offset as int:0
			pars.ids=ownerList
			
			transacaoInstanceList=Transacao.executeQuery("""select tr 
									from Transacao tr, Funcionario f
									where tr.participante=f  
									and f.unidade.rh.id in (:ids)
									order by tr.id desc""",	pars)
			
			transacaoInstanceTotal=Transacao.executeQuery("""select count(tr) from Transacao tr, Funcionario f
				where tr.participante=f 
				and f.unidade.rh.id in (:ids)""", [ids:ownerList])[0]
			
		}
		
        [transacaoInstanceList:transacaoInstanceList, transacaoInstanceTotal:transacaoInstanceTotal]
    }

    def create = {
        def transacaoInstance = new Transacao()
        transacaoInstance.properties = params
        return [transacaoInstance: transacaoInstance]
    }

    def save = {
        def transacaoInstance = new Transacao(params)
        if (transacaoInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'transacao.label', default: 'Transacao'), transacaoInstance.id])}"
            redirect(action: "show", id: transacaoInstance.id)
        }
        else {
            render(view: "create", model: [transacaoInstance: transacaoInstance])
        }
    }

    def show = {
        def transacaoInstance = Transacao.get(params.id)
        if (!transacaoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'transacao.label', default: 'Transacao'), params.id])}"
            redirect(action: "list")
        }
        else {
            [transacaoInstance: transacaoInstance]
        }
    }

    def edit = {
        def transacaoInstance = Transacao.get(params.id)
        if (!transacaoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'transacao.label', default: 'Transacao'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [transacaoInstance: transacaoInstance]
        }
    }

    def update = {
        def transacaoInstance = Transacao.get(params.id)
        if (transacaoInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (transacaoInstance.version > version) {
                    
                    transacaoInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'transacao.label', default: 'Transacao')] as Object[], "Another user has updated this Transacao while you were editing")
                    render(view: "edit", model: [transacaoInstance: transacaoInstance])
                    return
                }
            }
            transacaoInstance.properties = params
            if (!transacaoInstance.hasErrors() && transacaoInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'transacao.label', default: 'Transacao'), transacaoInstance.id])}"
                redirect(action: "show", id: transacaoInstance.id)
            }
            else {
                render(view: "edit", model: [transacaoInstance: transacaoInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'transacao.label', default: 'Transacao'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def transacaoInstance = Transacao.get(params.id)
        if (transacaoInstance) {
            try {
                transacaoInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'transacao.label', default: 'Transacao'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'transacao.label', default: 'Transacao'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'transacao.label', default: 'Transacao'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def agendarAll={
		flash.errors=[]
		Transacao.withTransaction{
			def errors=transacaoService.agendarAll()
			if(!errors)
				flash.message="Agendamento executando com SUCESSO"
			else
				flash.errors=errors
		}
		redirect(action:'list')
	}
	
	def simulador={}
	
	def autorizar={TransacaoCommand cmd->
		flash.errors=[]
		def transacaoInstance
		Transacao.withTransaction{
			try{
				def retorno=autorizadorService.autorizar(cmd)
				transacaoInstance=retorno.transacaoInstance
				if(transacaoInstance.autorizada)
					flash.message="Transacao ${transacaoInstance.nsu} AUTORIZADA"
				else{
					flash.errors<<"NÃO AUTORIZADA"
					flash.errors<<retorno.retorno
				}
			}catch(TransacaoException e){
				log.error e.message
				flash.errors<<"ERRO"
				flash.errors<<e.message
			}
			
		}
		render(view:"simulador",model:[transacaoInstance:transacaoInstance])
	}
	
}