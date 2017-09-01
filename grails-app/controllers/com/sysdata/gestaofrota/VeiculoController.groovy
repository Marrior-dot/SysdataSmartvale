package com.sysdata.gestaofrota

import grails.converters.*
import grails.plugins.springsecurity.Secured
import org.springframework.http.HttpStatus

import java.text.SimpleDateFormat

@Secured(['IS_AUTHENTICATED_FULLY'])
class VeiculoController extends BaseOwnerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def selectRhUnidade = {
        render(view: '/selectRhUnidade', model: [controller: "veiculo", action: Util.ACTION_NEW])
    }

    def list = {}

    def newList = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def criteria = {
            order('id')
        }
        def veiculoInstanceList = Veiculo.createCriteria().list(params, criteria)
        [veiculoInstanceList: veiculoInstanceList, veiculoInstanceTotal: Veiculo.count()]
    }

    def create = {
        if (params.unidade_id) {
            def unidadeInstance = Unidade.get(params.unidade_id)
            render(view: "form", model: [unidadeInstance: unidadeInstance, action: Util.ACTION_NEW])
        } else {
            flash.message = "Unidade não selecionada!"
            redirect(action: 'list')
        }
    }

    def save = {
        Veiculo.withTransaction {
            if (params.unidId) {
                def veiculoInstance = new Veiculo(params)
                def unidadeInstance = Unidade.get(params.unidId)
                veiculoInstance.unidade = unidadeInstance
                if (veiculoInstance.save(flush: true)) {
                    flash.message = "${message(code: 'default.created.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), veiculoInstance.id])}"
                    redirect(action: "show", id: veiculoInstance.id)
                } else {
                    render(view: "form", model: [veiculoInstance: veiculoInstance, unidadeInstance: veiculoInstance.unidade, action: Util.ACTION_NEW])
                }
            }
        }
    }

    def show = {
        def veiculoInstance = Veiculo.get(params.id)
        if (!veiculoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), params.id])}"
            redirect(action: "list")
        } else {
            render(view: 'form', model: [veiculoInstance: veiculoInstance, unidadeInstance: veiculoInstance.unidade, action: Util.ACTION_VIEW])
        }
    }

    def edit = {
        def veiculoInstance = Veiculo.get(params.id)
        if (!veiculoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), params.id])}"
            redirect(action: "list")
        } else {
            render(view: 'form', model: [veiculoInstance: veiculoInstance, unidadeInstance: veiculoInstance.unidade, action: 'editando'])
        }
    }

    def update = {
        def veiculoInstance = Veiculo.get(params.id)
        if (veiculoInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (veiculoInstance.version > version) {

                    veiculoInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'veiculo.label', default: 'Veiculo')] as Object[], "Another user has updated this Veiculo while you were editing")
                    render(view: 'form', model: [veiculoInstance: veiculoInstance, unidadeInstance: veiculoInstance.unidade, action: 'editando'])
                    return
                }
            }
            veiculoInstance.properties = params
            if (!veiculoInstance.hasErrors() && veiculoInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), veiculoInstance.id])}"
                redirect(action: "show", id: veiculoInstance.id)
            } else {
                render(view: 'form', model: [veiculoInstance: veiculoInstance, unidadeInstance: veiculoInstance.unidade, action: 'editando'])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def veiculoInstance = Veiculo.get(params.id)
        if (veiculoInstance) {
            try {
                veiculoInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), params.id])}"
                redirect(action: "show", params: [veicId: veiculoInstance.id])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), params.id])}"
            redirect(action: "list")
        }
    }



    def listAllJSON = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def offset = params.start ?: 10
        def opcao
        def filtro
        def unidId = params.unidade_id ? (params.unidade_id != 'null' ? params.unidade_id.toLong() : null) : null

        def veiculoInstanceList

        withSecurity { ownerList ->
            veiculoInstanceList = Veiculo
                    .createCriteria()
            //.list(max:params.max,offset:offset){
                    .list() {

                if (ownerList.size > 0)
                    unidade { rh { 'in'('id', ownerList) } }

                if (unidId)
                    unidade { eq('id', unidId) }
                if (params.opcao && params.filtro) {
                    opcao = params.opcao.toInteger()
                    filtro = params.filtro
                    //Placa
                    if (opcao == 1)
                        like('placa', filtro + '%')
                    //Marca
                    else if (opcao == 2)
                        like('modelo', filtro + '%')
                }
            }

        }

        def veiculoInstanceTotal

        withSecurity { ownerList ->
            veiculoInstanceTotal = Veiculo
                    .createCriteria()
                    .list() {

                if (ownerList.size > 0)
                    unidade { rh { 'in'('id', ownerList) } }

                if (unidId)
                    unidade { eq('id', unidId) }
                if (params.opcao && params.filtro) {
                    //Placa
                    if (opcao == 1)
                        like('placa', filtro + '%')
                    //Marca
                    else if (opcao == 2)
                        like('modelo', filtro + '%')
                }
                projections { rowCount() }
            }
        }


        def fields = veiculoInstanceList.collect { v ->
            [id    : v.id,
             placa : "<a href=${createLink(action: 'show')}/${v.id}>${v.placa}</a>",
             marca : v.marca?.nome,
             modelo: v.modelo,
             ano   : v.anoFabricacao
            ]
        }

        def data = [recordsTotal: veiculoInstanceTotal, results: fields]
        render data as JSON
    }


    def beforeInterceptor = [
            action: {
                def df = new SimpleDateFormat("dd/MM/yyyy")
                params.validadeExtintor = (params.validadeExtintor) ? df.parse(params.validadeExtintor) : null
            },
            only  : ['save', 'update']
    ]


    def getByPlaca = {
        def veiculoInstance = Veiculo.findByPlaca(params.placa)
        render "${veiculoInstance?.marca?.nome} ${veiculoInstance?.modelo}"
    }

	def listFuncionariosJSON={
		def veiculoInstance=Veiculo.get(params.id)
		def funcionariosList=veiculoInstance.funcionarios.collect {f->
				 					[id:f.funcionario.id,
									matricula:f.funcionario.matricula,
									 nome:f.funcionario.nome,
									 cpf:f.funcionario.cpf,
									 acao:"""<a href='#' onclick='removeFuncionario(${f.funcionario.id});'><img src='${resource(dir:'images',file:'remove_person.png')}' alt='Remover'></a>"""]
								}
		def data=[totalRecords:funcionariosList.size(),results:funcionariosList]
		render data as JSON
			
	}
	
	def addFuncionario={
		if(params.idVeic && params.idFunc){
			def veiculoInstance=Veiculo.get(params.idVeic)
			def funcionarioInstance=Funcionario.get(params.idFunc)
			if(veiculoInstance && funcionarioInstance){
				def veicFunc=new MaquinaFuncionario()
				veicFunc.funcionario=funcionarioInstance
				veiculoInstance.addToFuncionarios(veicFunc)
				if(veiculoInstance.save(flush:true))
					render "Funcionário relacionado ao Veículo com sucesso!"
				else
					render "Falha ao relacionar Funcionário ao Veículo!"
			}
		}else{
			render "Veículo ${params.idVeic} e/ou Funcionário ${params.idFunc} não encontrados!"
		}
	}
	
	def removeFuncionario={
		if(params.idVeic && params.idFunc){
			def idVeic=params.idVeic.toLong()
			def idFunc=params.idFunc.toLong()
			def veicFunc=MaquinaFuncionario.withCriteria(uniqueResult:true){
								maquina{eq('id',idVeic)}
								funcionario{eq('id',idFunc)}
							}
			veicFunc.delete()
			if(MaquinaFuncionario.exists(veicFunc.id))
				render "Relação entre Veículo e Funcionário removida"
			else
				render "Relação entre Veículo e Funcionário não pode ser removida, apenas desativada"
		}
	}
	

	
}