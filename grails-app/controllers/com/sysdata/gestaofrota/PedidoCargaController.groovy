package com.sysdata.gestaofrota

import grails.converters.JSON
import grails.plugins.springsecurity.Secured

import java.text.SimpleDateFormat

import org.apache.jasper.compiler.Node.ParamsAction;

import com.sysdata.gestaofrota.exception.InvalidCurrencyException

import com.sysdata.gestaofrota.Util


@Secured(['IS_AUTHENTICATED_FULLY'])
class PedidoCargaController extends BaseOwnerController {
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def springSecurityService
	

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {  }

	def newList = {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def criteria = {
			order('dateCreated', 'desc')
		}
		def pedidoCargaInstanceList = PedidoCarga.createCriteria().list(params, criteria)
		[pedidoCargaInstanceList:pedidoCargaInstanceList , pedidoCargaInstanceTotal: PedidoCarga.count()]
	}

	def create = {
		if(session.funcionariosList)
			session.funcionariosList=null
		
		if(params.unidade_id){
			def unidadeInstance=Unidade.get(params.unidade_id)
			
			//Calcula total inicial do pedido 
			
//			def vlTotPed=Funcionario.executeQuery("""
//select sum(c.valorCarga) from Funcionario f, CategoriaFuncionario c 
//where f.categoria=c 
//and f.unidade.id=:unid
//and f.status='ATIVO'""",[unid:unidadeInstance.id])[0]
			
			render(view:"form",model:[unidadeInstance:unidadeInstance,action:'novo'])
		}else{
			flash.message="Unidade não selecionada!"
			redirect(action:'list') 
		}
	}
	
	def selectRhUnidade={
		render(view:'/selectRhUnidade',model:[controller:"pedidoCarga",action:'novo'])
	}

	
	def synchServer(){
		
		def funcId=params.funcId.toInteger()
		def check=(params.check=="true")
		
		def funcList=session.funcionariosList
		
		def funcData=funcList.find{it.id==funcId}
		
		if(funcData){
			
			funcData['selecao']=check
			
			render "SUCESSO"
			response.status=response.SC_OK
		}else{
			render "ERRO INTERNO: Funcionário não localizado na lista!"
			response.status = response.SC_INTERNAL_SERVER_ERROR
		}
	}
	
	def synchCheckAll(){
		
		def categ=params.categ
		def check=(params.check=="true")
		
		if(categ=='all'){
			synchHttpSessionWithDB(session.funcionariosList,check)

			render "SUCESSO"
			response.status=response.SC_OK

		}else{
			def catId=params.categ as long
			
			def funcList=session.funcionariosList
			
			def foundList=funcList.findAll{it.categoria==catId}
			
			if(foundList){
				foundList.each{i->
					i['selecao']=check
				}
				render "SUCESSO"
				response.status=response.SC_OK
		
			}else{
				render "ERRO INTERNO: Categoria não localizado na lista!"
				response.status = response.SC_INTERNAL_SERVER_ERROR
			}
		}
	}
	
    def save = {
		PedidoCarga.withTransaction{
				
				def unidadeInstance=Unidade.get(params.unidId)
				def pedidoCargaInstance = new PedidoCarga(params)
				pedidoCargaInstance.unidade=unidadeInstance
				
				def validadeDias=ParametroSistema.getValorAsInteger(ParametroSistema.PEDIDO_VALIDADE)
				pedidoCargaInstance.validade=validadeDias
				
				/* Sincroniza lista de funcionários na sessão com o banco */
				def allFuncList=Funcionario.withCriteria {
												eq("status",Status.ATIVO)
												unidade{eq("id",unidadeInstance.id)}
											}
				
				synchHttpSessionWithDB(allFuncList,true)
				
				//Recupera para salvar no pedido apenas os funcionarios marcados 
				def funcionariosList=session.funcionariosList.findAll{it.selecao==true}
				
				if(funcionariosList){
					funcionariosList.each{f->
											
						def funcionarioInstance=Funcionario.get(f.id)
						pedidoCargaInstance.addToItens(new ItemPedido(participante:funcionarioInstance,
																	valor:Util.convertToCurrency(f.valorCarga),
																	sobra:0.00,
																	ativo:true
													))
					}
					pedidoCargaInstance.calcularTotal()
					
					if (pedidoCargaInstance.save(flush: true)) {
						
						clearSessionList()
						
						flash.message = "${message(code: 'default.created.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), pedidoCargaInstance.id])}"
						redirect(action: "show", id: pedidoCargaInstance.id)
					}
					else {
						render(view: "form", model: [pedidoCargaInstance: pedidoCargaInstance,unidadeInstance:unidadeInstance,action:Util.ACTION_NEW])
					}
						
				}else
					flash.message = "Nenhum funcionário selecionado para o pedido"
					render(view: "form", model: [pedidoCargaInstance: pedidoCargaInstance,unidadeInstance:unidadeInstance,action:Util.ACTION_NEW])
				
		}
		
    }

    def show = {
        def pedidoCargaInstance = PedidoCarga.get(params.id)
        if (!pedidoCargaInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), params.id])}"
            redirect(action: "list")
        }
        else {
            render(view:'form',model:[pedidoCargaInstance: pedidoCargaInstance,unidadeInstance:pedidoCargaInstance.unidade,action:Util.ACTION_VIEW])
        }
    }

    def edit = {
        def pedidoCargaInstance = PedidoCarga.get(params.id)
        if (!pedidoCargaInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), params.id])}"
            redirect(action: "list")
        }
        else {
			render view:'form',model:[pedidoCargaInstance: pedidoCargaInstance,unidadeInstance:pedidoCargaInstance.unidade,action:Util.ACTION_EDIT]
        }
    }

    def update = {
        def pedidoCargaInstance = PedidoCarga.get(params.id)
        if (pedidoCargaInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (pedidoCargaInstance.version > version) {
                    
                    pedidoCargaInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'pedidoCarga.label', default: 'PedidoCarga')] as Object[], "Another user has updated this PedidoCarga while you were editing")
                    render(view: "edit", model: [pedidoCargaInstance: pedidoCargaInstance])
                    return
                }
            }
			
			
			withListFromSession{ssList->
				/* Altera o valor das cargas nos itens conforme a listagem na sessão HTTP */
				
				ssList.each{f->
					
					def cargaInstance=pedidoCargaInstance.itens.find{it.participante.id==f.id}
					
					if(cargaInstance){
						def newValue=Util.convertToCurrency(f.valorCarga)
						if(cargaInstance.valor!=newValue){
							cargaInstance.valor=newValue
							cargaInstance.save(flush:true)
							log.debug "Valor da Carga ${cargaInstance.id} alterado no pedido ${pedidoCargaInstance.id}"
						}
					}
				}
				
				def delFuncList=ssList.findAll{it.selecao==false}
				
				delFuncList.each{d->
					ItemPedido.executeUpdate("delete from ItemPedido i where i.id=:id",[id:d.item])
					ssList.remove(d)
				}
	
			}
			
            pedidoCargaInstance.properties = params
            
			pedidoCargaInstance.calcularTotal()
			if (!pedidoCargaInstance.hasErrors() && pedidoCargaInstance.save(flush: true)) {
				
				clearSessionList()
				
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), pedidoCargaInstance.id])}"
                redirect(action: "show", id: pedidoCargaInstance.id)
            }
            else {
                render(view: "edit", model: [pedidoCargaInstance: pedidoCargaInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def pedidoCargaInstance = PedidoCarga.get(params.id)
        if (pedidoCargaInstance) {
            try {
                pedidoCargaInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def listAllJSON={
		
		clearSessionList()
		
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def offset=params.offset?params.offset as int:10
		def opcao
		def filtro
		def unidId=params.unidade_id!='null'?params.unidade_id.toLong():null

		def pedidoCargaInstanceList
		
		withSecurity{ownerList->
			pedidoCargaInstanceList=PedidoCarga.withCriteria{
										
										if(ownerList.size>0)
											unidade{rh{'in'('id',ownerList)}}
										
										if(unidId)
											unidade{eq('id',unidId)}
										
										firstResult(offset)
										maxResults(params.max)
														
										order("id","desc")
									}

		}
		
		def pedidoCargaInstanceTotal
		
		withSecurity{ownerList->
			pedidoCargaInstanceTotal=PedidoCarga
										.createCriteria()
										.list(){
											
											if(ownerList.size>0)
												unidade{rh{'in'('id',ownerList)}}
											
											if(unidId)
												unidade{eq('id',unidId)}
											projections{ rowCount() }
										}
			
		}
		

		
		def fields=pedidoCargaInstanceList.collect{p->
			[id:p.id,
			rh:p.unidade.rh.nome,
			unidade:p.unidade.nome,
			dataPedido:Util.formattedDate(p.dateCreated),
			dataCarga:Util.formattedDate(p.dataCarga),
			total:Util.formatCurrency(p.total),
			status:p.status.nome,
			acoes:"""
					<a class='show' title='Visualizar Detalhes' href='${createLink(action:'show')}/${p.id}'>  
					${if(p.status==StatusPedidoCarga.NOVO && !(springSecurityService.currentUser.owner instanceof Rh))
					" <a class='release' title='Liberar Pedido' href='${createLink(action:'releasePedido',id:p.id)}'></a>" else "" }

 					${if(p.status==StatusPedidoCarga.NOVO)					
					" <a class='cancel' title='Cancelar Pedido' href='${createLink(action:'cancelPedido',id:p.id)}'></a>"
					else
					""	
					}"""]
		}

		def data=[totalRecords:pedidoCargaInstanceTotal,results:fields]
		render data as JSON
	}
	
	/*
	 * Altera status do pedido para LIBERADO
	 * Insere transação de CARGA para cada item do pedido
	 * 
	 */
	@Secured(["ROLE_ADMIN","ROLE_PROC"])
	def releasePedido={
		def pedidoCargaInstance=PedidoCarga.get(params.id)
		
		if(pedidoCargaInstance.status==StatusPedidoCarga.NOVO){
			pedidoCargaInstance.status=StatusPedidoCarga.LIBERADO
			//Registra o usuário que realizou a liberação
			pedidoCargaInstance.usuario=springSecurityService.currentUser
			
			//Registra Transação de Carga para posterior Agendamento
			pedidoCargaInstance.itens.each{item->
			
				new Transacao(participante:item.participante,
								valor:item.valor,
								status:StatusTransacao.AGENDAR,
								tipo:TipoTransacao.CARGA_SALDO).
				save(flush:true)
					
			}
			
			if(pedidoCargaInstance.save(flush:true))
				flash.message="Pedido ${pedidoCargaInstance.id} LIBERADO"
			else
				flash.message="ERRO ao Salvar. Pedido ${pedidoCargaInstance.id} não liberado"
		}else
			flash.message="Pedido ${pedidoCargaInstance.id} não pode ser LIBERADO. Status Inválido"
		
		redirect(action:'list')
	}
	
	/*
	 * Altera status do pedido para CANCELADO 
	 * Altera status dos itens do pedido para INATIVO
	 */
	 
	def cancelPedido={
		def pedidoCargaInstance=PedidoCarga.get(params.id)
		
		if(pedidoCargaInstance.status==StatusPedidoCarga.NOVO){
			pedidoCargaInstance.status=StatusPedidoCarga.CANCELADO
			
			PedidoCarga.executeUpdate("update ItemPedido i set i.ativo=false where i.pedido=:pedido",[pedido:pedidoCargaInstance])
			
			if(pedidoCargaInstance.save(flush:true))
				flash.message="Pedido ${pedidoCargaInstance.id} CANCELADO"
			else
				flash.message="ERRO ao Salvar. Pedido ${pedidoCargaInstance.id} não cancelado"
			
		}else
			flash.message="Pedido ${pedidoCargaInstance.id} não pode ser CANCELADO. Status inválido"
		
		redirect(action:'list')
	}
	
	def listFuncionariosPedidoJSON={
		
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		params.offset=params.offset?params.int('offset'):0
		
		params.categId=(params.categId)?params.categId.toLong():null
		
		//Tenta localizar pelo critério de busca primeiramente na lista da HTTP Session
		
		def qryList="""select i from ItemPedido i, Funcionario f where i.pedido=:pedido and i.participante=f
			${if(params.categId) ' and f.categoria.id='+params.categId else ''} """
		
		def qryCount="""select count(i.id) from ItemPedido i, Funcionario f where i.pedido=:pedido and i.participante=f
			${if(params.categId) ' and f.categoria.id='+params.categId else ''}"""
		
		def filter
				
		if(params.opcao && params.filtro){
			params.opcao=params.opcao as int
			
			if(params.opcao==1)
				filter=""" and f.matricula like '${params.filtro}%'"""
			
			else if(params.opcao==2)
				filter=""" and f.nome like '${params.filtro}%'"""
				
			else if(params.opcao==3)
				filter=""" and f.cpf like '${params.filtro}%'"""
		}
			
		if(filter){
			qryList+=filter
			qryCount+=filter
		}
		
		qryList+=" order by f.nome"
		
		def pedidoCargaInstance=PedidoCarga.get(params.id)
		
		def funcionarioInstanceList=Transacao.executeQuery(qryList,[max:params.max,offset:params.offset,pedido:pedidoCargaInstance])
	
		def funcionarioInstanceTotal=Transacao.executeQuery(qryCount,[pedido:pedidoCargaInstance])

		def retList
		def offsetList
		
		withListFromSession{ssList->
			 
			//Sincroniza objetos para lista HTTP Session
             funcionarioInstanceList.each{i->
             
	             retList=[
	                       id:i.participante.id,
	                       categoria:i.participante.categoria.id,
	                       matricula:i.participante.matricula,
	                       nome:i.participante.nome,
	                       cpf:i.participante.cpf,
	                       valorCarga:Util.formatCurrency(i.valor),
	                       selecao:true,
	                       item:i.id
	                       ]
                       
	               if(!ssList.find{it.id==i.participante.id}){
	            	   ssList<<retList
	               }
			 }
			 
			 offsetList=paginateInSession(filterByCriteriaInSession(ssList,params),params)
			 
		}
		
		def data=[totalRecords:funcionarioInstanceTotal,results:offsetList]
		
		response.setHeader("Cache-control", "no-store")
		render data as JSON
										
	}

	/*
	* Realiza paginação em lista filtrada na HTTP Session
	*/

	
	private def paginateInSession(filteredList,pars){
		def offsetList=[]
		filteredList.eachWithIndex{item,i->
			if(i+1>pars.offset && i+1<=pars.offset+params.max)
				offsetList<<item
		}
		
		offsetList
	}
	
	
	/*
	 * Filtra por criterio na lista da HTTP Session
	 */
	
	private def filterByCriteriaInSession(ssList,pars){
		ssList.findAll{
						pars.categId?it.categoria==pars.categId:true &&
						pars.opcao==1?it.matricula.startsWith(pars.filtro):true &&
						pars.opcao==2?it.nome.startsWith(pars.filtro):true &&
						pars.opcao==3?it.cpf.startsWith(pars.filtro):true

			}
	}
	
	private def clearSessionList(){
		withListFromSession{ssList->
			ssList.clear()
			ssList=null
		}
	}
	
	
	/*
	* Varre a lista na HTTP Session e
	* a completa sob demanda com os novos dados oriundos do banco
	* */
   
   private def synchHttpSessionWithDB(dbFuncList,check){
	   
	   withListFromSession{ssList->
		   
		   dbFuncList.each{f->
			   
			   //Só inclui na lista da HTTP Session os dados ainda não incluidos anteriormente
			   if(!ssList.find{it.id==f.id}){
				   
				   ssList<<[
							   id:f.id,
							   categoria:f.categoria.id,
							   matricula:f.matricula,
							   nome:f.nome,
							   cpf:f.cpf,
							   valorCarga:Util.formatCurrency(f.categoria?.valorCarga),
							   selecao:check
						   ]
			   }
   
		   }
	   }

   }

	
	def listFuncionariosCategoriaJSON={
		
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		params.offset=params.offset?params.int('offset'):0
		def unidId=(params.unidade_id && params.unidade_id!='null')?params.unidade_id.toLong():null
		params.categId=(params.categId)?params.categId.toLong():null
		
		def funcionarioInstanceList=Funcionario
										.createCriteria()
										.list(max:params.max,offset:params.offset){
											eq("status",Status.ATIVO)
											
											if(unidId)
												unidade{eq('id',unidId)}
											if(params.categId)
												categoria{eq('id',params.categId)}
											if(params.opcao && params.filtro){
												params.opcao=params.opcao as int
												//Matricula
											if(params.opcao==1)
												like('matricula',params.filtro+'%')
												//Nome
											else if(params.opcao==2)
												like('nome',params.filtro+'%')
												//CPF
											else if(params.opcao==3)
												like('cpf',params.filtro+'%')
											}
											order("nome")
										}
		def funcionarioInstanceTotal=Funcionario
											.createCriteria()
											.list(){
												eq("status",Status.ATIVO)
												if(unidId)
													unidade{eq('id',unidId)}
												if(params.categId)
													categoria{eq('id',params.categId)}
												if(params.opcao && params.filtro){
													//Matricula
													if(params.opcao==1)
														like('matricula',params.filtro+'%')
														//Nome
													else if(params.opcao==2)
														like('nome',params.filtro+'%')
													//CPF
													else if(params.opcao==3)
														like('cpf',params.filtro+'%')
												}
												projections{ rowCount() }
										}
				
			
		synchHttpSessionWithDB(funcionarioInstanceList, true)
		
		def offsetList
		
		withListFromSession{ssList->
			offsetList=paginateInSession(filterByCriteriaInSession(ssList,params),params)
		}
		
			
		def dataJSON=[totalRecords:funcionarioInstanceTotal,results:offsetList]
							
		response.setHeader("Cache-control", "no-store")
		
		render dataJSON as JSON
	}
	
	def updateValorCarga={
		if(session.funcionariosList){
			def field=params.field
			def idField=params.id.toInteger()
			try{
				def currVal=Util.convertToCurrency(params.newValue)
				def funcionarioList=session.funcionariosList
				def func=funcionarioList.find{it.id==idField}
				if(func){
					func['valorCarga']=Util.formatCurrency(currVal)
					render "SUCESSO"
					response.status=response.SC_OK
					return
				}else
					render "ERRO INTERNO: Funcionário não localizado na lista!"
					response.status = response.SC_INTERNAL_SERVER_ERROR
					
			}catch(InvalidCurrencyException e){
				render e.message
				response.status = response.SC_INTERNAL_SERVER_ERROR
			}
		}else{
			session.invalidate()
			response.status = response.SC_INTERNAL_SERVER_ERROR
		}
	}
	
	def beforeInterceptor=[
		action:{
			def df=new SimpleDateFormat("dd/MM/yyyy")
			params.dataCarga=(params.dataCarga)?df.parse(params.dataCarga):null
		},
		only:['save','update']
	]
	
	def withListFromSession={cls->
		def sessFuncList=session.funcionariosList
		
		if(!sessFuncList) sessFuncList=[]
		
		cls.call(sessFuncList)
		
		session.funcionariosList=sessFuncList

	}
	
}
