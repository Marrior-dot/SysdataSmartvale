package com.sysdata.gestaofrota

import grails.converters.JSON
import java.text.SimpleDateFormat

class FuncionarioController extends BaseOwnerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def funcionarioService
    def processamentoService

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        Participante participanteInstance = getCurrentUser()?.owner
        Unidade unidadeInstance = null
        if (participanteInstance?.instanceOf(Rh)) {
            Rh rh = Rh.get(participanteInstance.id)
            unidadeInstance = Unidade.findByRh(rh)
        }

        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def criteria = {
            order('id')

            if (unidadeInstance) {
                eq('unidade', unidadeInstance)
            }
        }

        def funcionarioInstanceList = Funcionario.createCriteria().list(params, criteria)
        [funcionarioInstanceList: funcionarioInstanceList, funcionarioInstanceTotal: Rh.count()]
    }

    def selectRhUnidade() {
        render(view: '/selectRhUnidade', model: [controller: "funcionario", action: Util.ACTION_NEW])
    }

    def sugestoes() {
        try {
            def sugestoes = funcionarioService.sugestoes(params?.nome)
            render([sug: sugestoes] as JSON)
        } catch (e){
            println(e.message)
            render(['erro'] as JSON)
        }
    }

    def create() {
        Unidade unidadeInstance = Unidade.get(params.long('unidade_id'))
        if (unidadeInstance) {

            if (unidadeInstance.rh.modeloCobranca == TipoCobranca.PRE_PAGO &&
                    unidadeInstance.rh.vinculoCartao == TipoVinculoCartao.FUNCIONARIO &&
                    CategoriaFuncionario.porUnidade(unidadeInstance).count() == 0) {
                flash.error = "Não existe nenhum Perfil de Recarga definido. É necessário cadastrar um primeiro."
                redirect(controller: 'rh', action: 'show', id: unidadeInstance?.rh?.id)
                return
            }

            Funcionario funcionario = new Funcionario(unidade: unidadeInstance)
            if (unidadeInstance.rh.vinculoCartao == TipoVinculoCartao.FUNCIONARIO) {
                funcionario.portador = new PortadorFuncionario()
                funcionario.portador.unidade = unidadeInstance
            }

            // TODO: Refatorar Embossing
            render(view: "form", model: [funcionarioInstance: funcionario,
                                         action             : Util.ACTION_NEW,
                                         tamMaxEmbossing    : processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
        } else {
            flash.message = "Unidade não selecionada!"
            redirect(action: 'list')
        }
    }

    def save() {
        Funcionario funcionarioInstance = new Funcionario(params)
        try {
            def ret = funcionarioService.save(funcionarioInstance, true)

            if (ret.success) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), funcionarioInstance.id])}"
                redirect(action: "show", id: funcionarioInstance.id)
            } else {
                if (ret.message)
                    flash.error = ret.message
                render(view: "form", model: [funcionarioInstance: funcionarioInstance, unidadeInstance: funcionarioInstance.unidade, action: Util.ACTION_NEW, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
            }

        }
        catch (Exception e) {
            e.printStackTrace()
            flash.error = "Erro Interno. Contatar suporte"
            render(view: "form", model: [funcionarioInstance: funcionarioInstance, action: Util.ACTION_NEW, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
        }

    }

    def show() {
        def funcionarioInstance = Funcionario.get(params.long('id'))
        if (! funcionarioInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
            redirect(action: "list")
        } else {
            render(view: 'form', model: [funcionarioInstance: funcionarioInstance, unidadeInstance: funcionarioInstance.unidade, action: Util.ACTION_VIEW, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
        }
    }

    def edit() {
        Funcionario funcionarioInstance = Funcionario.get(params.id)
        if (!funcionarioInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'funcionario.label', default: 'Funcionário'), params.id])}"
            redirect(action: "list")
        } else {
            render(view: 'form', model: [funcionarioInstance: funcionarioInstance, unidadeInstance: funcionarioInstance.unidade, action: Util.ACTION_EDIT, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
        }
    }

    def update() {
        def funcionarioInstance = Funcionario.get(params.long('id'))
        if (funcionarioInstance) {
            try {
                funcionarioInstance.properties = params
                def ret = funcionarioService.save(params, funcionarioInstance)
                if (ret.success) {
                    flash.message = "${message(code: 'default.updated.message', args: [message(code: 'funcionario.label', default: 'Funcionário'), funcionarioInstance.id])}"
                    redirect(action: "show", id: funcionarioInstance.id)
                } else {
                    if (ret.message)
                        flash.error = ret.message

                    render(view: "form", model: [funcionarioInstance: funcionarioInstance, unidadeInstance: funcionarioInstance.unidade, action: Util.ACTION_EDIT, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
                }

            }
            catch (Exception e) {
                e.printStackTrace()
                flash.error = e.message
                render(view: "form", model: [funcionarioInstance: funcionarioInstance, unidadeInstance: funcionarioInstance.unidade, action: Util.ACTION_EDIT, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
            }

        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete() {
        Funcionario funcionarioInstance = Funcionario.get(params.id as long)
        if (funcionarioInstance) {
            try {
                def unidId = funcionarioInstance.unidade.id
                funcionarioService.delete(funcionarioInstance)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
                redirect(controller: "unidade", action: "show", id: unidId)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                e.printStackTrace()
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
                redirect(action: "show", id: funcionarioInstance.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
            redirect(controller: "unidade", action: "list")
        }
    }

    def listAllJSON() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def offset = params.start ?: 0
        def opcao
        def filtro
        def unidId = params.unidade_id ? (params.unidade_id != 'null' ? params.unidade_id.toLong() : null) : null
        def categId = params.categId
        def gestor = params.gestor


        def funcionarioInstanceList
        def funcionarioInstanceTotal


        def criteria = {

            eq('status', Status.ATIVO)

            if (unidId)
                unidade { eq('id', unidId) }

            if (categId)
                categoria { eq('id', categId) }

            if (gestor && gestor != "null")
                eq("gestor", true)

            if (params.opcao && params.filtro) {
                opcao = params.opcao.toInteger()
                filtro = params.filtro
                //Matricula
                if (opcao == 1)
                    like('matricula', filtro + '%')
                //Nome
                else if (opcao == 2)
                    like('nome', filtro + '%')
                //CPF
                else if (opcao == 3)
                    like('cpf', filtro + '%')
            }

        }

        funcionarioInstanceList = Funcionario.createCriteria().list([order: 'nome', max: params.max, offset: offset], criteria)
        funcionarioInstanceTotal = Funcionario.createCriteria().count(criteria)


        def fields = funcionarioInstanceList.collect { f ->
            [
                    id       : f.id,
                    matricula: f.matricula,
                    nome     : f.nome,
                    cartao   : f?.portador?.cartaoAtivo?.numeroMascarado ?: '< Nenhum cartão ativo >',
                    cpf      : "<a href=${createLink(action: 'show', id: f.id)}>${f.cpf}</a>"
            ]

        }

        def data = [recordsTotal: funcionarioInstanceTotal, results: fields]

        render data as JSON
    }

    def beforeInterceptor = [
            action: {
                def df = new SimpleDateFormat("dd/MM/yyyy")
                params.dataNascimento = (params.dataNascimento) ? df.parse(params.dataNascimento) : null
                params.validadeCnh = (params.validadeCnh) ? df.parse(params.validadeCnh) : null
            },
            only  : ['save', 'update']
    ]

    def search() {
        //render(template:"search",model:[controller:params.controller,unidade_id:params.unidade_id,action:params.action])
        render template: "search", model: params
    }

    def getByMatricula = {
        def funcionarioInstance = Funcionario.findByMatricula(params.matricula)
        render "${funcionarioInstance?.nome}"
    }

    def generateNewPassword() {
        def ret = [:]

        def cartaoInstance = Cartao.get(params.id)

        if (cartaoInstance) {
            def newPsw = funcionarioService.gerarSenha()
            cartaoInstance.senha = newPsw
            cartaoInstance.save(flush: true)
        }
        ret['newPsw'] = cartaoInstance.senha
        render ret as JSON
    }
}
