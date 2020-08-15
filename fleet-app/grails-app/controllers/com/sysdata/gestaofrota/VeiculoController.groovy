package com.sysdata.gestaofrota

import grails.converters.JSON

import java.text.SimpleDateFormat

class VeiculoController  {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    VeiculoService veiculoService
    ProcessamentoService processamentoService
    def springSecurityService

    def index() {
        redirect(action: "list", params: params)
    }

    def selectRhUnidade = {
        render(view: '/selectRhUnidade', model: [controller: "veiculo", action: Util.ACTION_NEW])
    }

    def list = {}

    def newList() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def criteria = {
            order('id')
        }
        def veiculoInstanceList = Veiculo.createCriteria().list(params, criteria)
        [veiculoInstanceList: veiculoInstanceList, veiculoInstanceTotal: Veiculo.count()]
    }

    def create() {
        if (params.unidade_id) {
            def unidadeInstance = Unidade.get(params.unidade_id)

            if (unidadeInstance.rh.modeloCobranca == TipoCobranca.PRE_PAGO &&
                    unidadeInstance.rh.vinculoCartao == TipoVinculoCartao.MAQUINA &&
                    CategoriaFuncionario.porUnidade(unidadeInstance).count() == 0) {

                flash.error = "Não existe nenhum Perfil de Recarga definido. É necessário cadastrar um primeiro."
                redirect(controller: 'rh', action: 'show', id: unidadeInstance?.rh?.id)
                return
            }

            Veiculo veiculo = new Veiculo(params)
            veiculo.unidade = unidadeInstance

            if (unidadeInstance.rh.vinculoCartao == TipoVinculoCartao.MAQUINA) {
                veiculo.portador = new PortadorMaquina()
                veiculo.portador.unidade = unidadeInstance
            }

            int tamMaxEmbossing = processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()
            render(view: "form", model: [veiculoInstance: veiculo, action: Util.ACTION_NEW, tamMaxEmbossing: tamMaxEmbossing])
        } else {
            flash.message = "Unidade não selecionada!"
            redirect(action: 'list')
        }
    }

    def save(Veiculo veiculoInstance) {

        if (veiculoInstance) {
            try {
                def ret = veiculoService.save(veiculoInstance)

                if (ret.success) {
                    flash.message = "${message(code: 'default.created.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), veiculoInstance.id])}"
                    redirect(action: "show", id: veiculoInstance.id)
                } else {
                    if (ret.message)
                        flash.error = ret.message
                    render(view: "form", model: [veiculoInstance: veiculoInstance, action: Util.ACTION_NEW, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
                }
            }
            catch (Exception e) {
                flash.error = "Veículo não pode ser salvo. Contate suporte."
                e.printStackTrace()
                int tamMaxEmbossing = processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()
                render(view: "form", model: [veiculoInstance: veiculoInstance, unidadeInstance: veiculoInstance.unidade, action: Util.ACTION_NEW, tamMaxEmbossing: tamMaxEmbossing])
            }

        } else {
            flash.error = "Unidade não encontrada."
            redirect(action: 'index')
        }
    }

    def show() {
        def veiculoInstance = Veiculo.get(params.long('id'))
        if (!veiculoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), params.id])}"
            redirect(action: "list")
        } else {
            int tamMaxEmbossing = processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()
            render(view: "form", model: [veiculoInstance: veiculoInstance, unidadeInstance: veiculoInstance.unidade, action: Util.ACTION_VIEW, tamMaxEmbossing: tamMaxEmbossing])
        }
    }

    def edit() {
        def veiculoInstance = Veiculo.get(params.id)
        if (!veiculoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), params.id])}"
            redirect(action: "list")
        } else {
            int tamMaxEmbossing = processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()
            render(view: 'form', model: [veiculoInstance: veiculoInstance, unidadeInstance: veiculoInstance.unidade, action: 'editando', tamMaxEmbossing: tamMaxEmbossing])
        }
    }

    def update(Veiculo veiculoInstance) {
        if (veiculoInstance) {
            veiculoInstance.properties = params
            try {
                veiculoService.save(veiculoInstance, params)
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), veiculoInstance.id])}"
                redirect(action: "show", id: veiculoInstance.id)
            } catch (e) {
                e.printStackTrace()
                flash.error = "Erro Interno. Contatar suporte"
                int tamMaxEmbossing = processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()
                render(view: 'form', model: [veiculoInstance: veiculoInstance, unidadeInstance: veiculoInstance.unidade, action: 'editando', tamMaxEmbossing: tamMaxEmbossing])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete(Veiculo veiculoInstance) {
        if (veiculoInstance) {
            try {
                def unidId = veiculoInstance.unidade.id
                veiculoService.delete(veiculoInstance)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), params.id])}"
                redirect(controller: "unidade", action: "show", id: unidId)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), params.id])}"
                redirect(action: "show", params: [veicId: veiculoInstance.id])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'veiculo.label', default: 'Veiculo'), params.id])}"
            redirect(controller: "unidade", action: "list")
        }
    }


    def listAllJSON() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        params.offset = params.start ?: 10
        def opcao
        def filtro
        def unidId = params.unidade_id ? (params.unidade_id != 'null' ? params.unidade_id.toLong() : null) : null

        def veiculoInstanceList
        def veiculoInstanceTotal

//        withSecurity { ownerList ->

            def criteria = {
                eq("status", Status.ATIVO)

                if (unidId) {
                    unidade { eq('id', unidId) }
                }
/*
                else if (ownerList.size() > 0)
                    unidade { rh { 'in'('id', ownerList) } }
*/

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

            veiculoInstanceList = Veiculo.createCriteria().list(params: params, criteria)
            veiculoInstanceTotal = Veiculo.createCriteria().count(criteria)
//        }

        def fields = veiculoInstanceList.collect { v ->
            [id    : v.id,
             placa : "<a href=${createLink(action: 'show')}/${v.id}>${v.placa}</a>",
             marca : v.marca?.nome,
             modelo: v.modelo,
             ano   : v.anoFabricacao,
             cartao: v?.portador?.cartaoAtivo?.numeroMascarado ?: '< Nenhum cartão ativo >',
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


    def getByPlaca() {
        def veiculoInstance = Veiculo.findByPlaca(params.placa)
        render "${veiculoInstance?.marca?.nome} ${veiculoInstance?.modelo}"
    }

    def listFuncionariosJSON() {
        def veiculoInstance = Veiculo.get(params.id)
        def funcionariosList = veiculoInstance.funcionarios.collect { f ->
            [id       : f.funcionario.id,
             matricula: f.funcionario.matricula,
             nome     : f.funcionario.nome,
             cpf      : f.funcionario.cpf,
             acao     : """<a href='#' onclick='removeFuncionario(${f.funcionario.id});'><img src='${
                 resource(dir: 'images', file: 'remove_person.png')
             }' alt='Remover'></a>"""]
        }
        def data = [totalRecords: funcionariosList.size(), results: funcionariosList]
        render data as JSON

    }

    def addFuncionario() {
        if (params.idVeic && params.idFunc) {
            def veiculoInstance = Veiculo.get(params.idVeic)
            def funcionarioInstance = Funcionario.get(params.idFunc)
            if (veiculoInstance && funcionarioInstance) {
                def veicFunc = new MaquinaFuncionario()
                veicFunc.funcionario = funcionarioInstance
                veiculoInstance.addToFuncionarios(veicFunc)
                if (veiculoInstance.save(flush: true))
                    render "Funcionário relacionado ao Veículo com sucesso!"
                else
                    render "Falha ao relacionar Funcionário ao Veículo!"
            }
        } else {
            render "Veículo ${params.idVeic} e/ou Funcionário ${params.idFunc} não encontrados!"
        }
    }

    def removeFuncionario() {
        if (params.idVeic && params.idFunc) {
            def idVeic = params.idVeic.toLong()
            def idFunc = params.idFunc.toLong()
            def veicFunc = MaquinaFuncionario.withCriteria(uniqueResult: true) {
                maquina { eq('id', idVeic) }
                funcionario { eq('id', idFunc) }
            }
            veicFunc.delete()
            if (MaquinaFuncionario.exists(veicFunc.id))
                render "Relação entre Veículo e Funcionário removida"
            else
                render "Relação entre Veículo e Funcionário não pode ser removida, apenas desativada"
        }
    }

    def alterarHodometro() {
        int tamMaxEmbossing = processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()
        def valor = params.hodometro as long
        User user = springSecurityService.currentUser
        def veiculoInstance = Veiculo.get(params.id)
        if(veiculoService.alteraHodometro(veiculoInstance,valor, user)){
            flash.message = "Hodômetro alterado com sucesso."
        }else{
            flash.message = "Um erro ocorreu no momento de alterar o hodômetro. Tente novamente ou contate o suporte."
        }
        render(view: "form", model: [veiculoInstance: veiculoInstance, unidadeInstance: veiculoInstance.unidade, action: Util.ACTION_VIEW,tamMaxEmbossing: tamMaxEmbossing])
    }

}