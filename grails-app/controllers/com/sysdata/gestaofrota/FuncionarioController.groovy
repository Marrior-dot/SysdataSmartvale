package com.sysdata.gestaofrota

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import java.text.SimpleDateFormat

@Secured(['IS_AUTHENTICATED_FULLY'])
class FuncionarioController extends BaseOwnerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def funcionarioService
    def processamentoService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
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

    def selectRhUnidade = {
        render(view: '/selectRhUnidade', model: [controller: "funcionario", action: Util.ACTION_NEW])
    }

    def create = {
        Unidade unidadeInstance = Unidade.get(params.long('unidade_id'))
        if (unidadeInstance) {
            if (CategoriaFuncionario.porUnidade(unidadeInstance).count() == 0) {
                flash.error = "Não existe nenhuma Categoria de Funcionário. É necessário cadastrar uma primeiro."
                redirect(controller: 'rh', action: 'show', id: unidadeInstance?.rh?.id)
                return
            }
            Funcionario funcionario = new Funcionario()
            funcionario.portador = new PortadorFuncionario()
            render(view: "form", model: [funcionario    : funcionario,
                                         unidadeInstance: unidadeInstance,
                                         action         : Util.ACTION_NEW,
                                         tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
        } else {
            flash.message = "Unidade não selecionada!"
            redirect(action: 'list')
        }
    }

    def save = {
        Funcionario funcionarioInstance = new Funcionario(params)
        Unidade unidadeInstance = Unidade.get(params.long('unidId'))

        if (unidadeInstance) {
            try {
                funcionarioInstance.unidade = unidadeInstance
                funcionarioInstance.endereco = params['endereco']
                funcionarioInstance.telefone = params['telefone']

                if(funcionarioInstance.unidade.rh.vinculoCartao==TipoVinculoCartao.FUNCIONARIO){
                    funcionarioInstance.portador = new PortadorFuncionario()
                    funcionarioInstance.portador.valorLimite = params.double('portador.valorLimite')
                    funcionarioInstance.portador.tipoLimite = TipoLimite.valueOf(params['portador.tipoLimite'])
                    funcionarioInstance.portador.unidade = unidadeInstance
                }

                funcionarioInstance = funcionarioService.save(funcionarioInstance, true)

                flash.message = "${message(code: 'default.created.message', args: [message(code: 'funcionario.label', default: 'Funcionaroi'), funcionarioInstance.id])}"
                redirect(action: "show", id: funcionarioInstance.id)
            }
            catch (Exception e) {

                log.error "Erro: $e.message"
                flash.error = "Um erro ocorreu."
                render(view: "form", model: [funcionarioInstance: funcionarioInstance, unidadeInstance: funcionarioInstance.unidade, action: Util.ACTION_NEW, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
            }
        } else {
            flash.message = "Funcionário não relacionado a uma Unidade específica."
            render(view: "form", model: [funcionarioInstance: funcionarioInstance, unidadeInstance: funcionarioInstance.unidade, action: Util.ACTION_NEW, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
        }
    }

    def show = {
        def funcionarioInstance = Funcionario.get(params.long('id'))
        if (!funcionarioInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
            redirect(action: "list")
        } else {
            render(view: 'form', model: [funcionarioInstance: funcionarioInstance, unidadeInstance: funcionarioInstance.unidade, action: Util.ACTION_VIEW, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
        }
    }

    def edit = {
        Funcionario funcionarioInstance = Funcionario.get(params.id)
        if (!funcionarioInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'funcionario.label', default: 'Funcionário'), params.id])}"
            redirect(action: "list")
        } else {
            render(view: 'form', model: [funcionarioInstance: funcionarioInstance, unidadeInstance: funcionarioInstance.unidade, action: Util.ACTION_EDIT, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
        }
    }

    def update = {
        def funcionarioInstance = Funcionario.get(params.long('id'))
        if (funcionarioInstance) {
            Long version = params.long('version')
            if (version && funcionarioInstance.version > version) {
                funcionarioInstance.errors.rejectValue("version", "Outro usuário estava alterando os dados desse Funcionário enquanto você o estava editando.")
                render(view: 'form', model: [funcionarioInstance: funcionarioInstance, unidadeInstance: funcionarioInstance.unidade, action: Util.ACTION_EDIT, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
                return
            }

            try {
                funcionarioInstance.properties = params
                funcionarioInstance.endereco = params['endereco']
                funcionarioInstance.telefone = params['telefone']
                funcionarioInstance.portador.valorLimite = params.double('portador.valorLimite')
                funcionarioInstance.portador.tipoLimite = TipoLimite.valueOf(params['portador.tipoLimite'])

                funcionarioInstance = funcionarioService.save(funcionarioInstance)

                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'funcionario.label', default: 'Funcionário'), funcionarioInstance.id])}"
                redirect(action: "show", id: funcionarioInstance.id)
            }
            catch (Exception e) {
                println(e.message)
                render(view: "form", model: [funcionarioInstance: funcionarioInstance, unidadeInstance: funcionarioInstance.unidade, action: Util.ACTION_NEW, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
            }

        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def funcionarioInstance = Funcionario.get(params.long('id'))
        if (funcionarioInstance) {
            try {
                log.debug("Inativando " + funcionarioInstance)
                funcionarioInstance.status = Status.INATIVO
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
                redirect(action: "show", params: [funcId: funcionarioInstance.id])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'funcionario.label', default: 'Funcionario'), params.id])}"
            redirect(action: "list")
        }
    }

    def listAllJSON = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def offset = params.start ?: 0
        def opcao
        def filtro
        def unidId = params.unidade_id ? (params.unidade_id != 'null' ? params.unidade_id.toLong() : null) : null
        def categId = params.categId
        def gestor = params.gestor

        def funcionarioInstanceList

        withSecurity { ownerList ->
            funcionarioInstanceList = Funcionario.createCriteria().list() {
                eq('status', Status.ATIVO)
                if (ownerList.size > 0)
                    unidade { rh { 'in'('id', ownerList) } }

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

                order("nome")
            }
        }

        def funcionarioInstanceTotal

        withSecurity { ownerList ->
            funcionarioInstanceTotal = Funcionario
                    .createCriteria()
                    .list() {
                eq('status', Status.ATIVO)
                if (ownerList.size > 0)
                    unidade { rh { 'in'('id', ownerList) } }

                if (unidId)
                    unidade { eq('id', unidId) }

                if (categId)
                    categoria { eq('id', categId) }

                if (gestor != "null")
                    eq("gestor", true)

                if (params.opcao && params.filtro) {
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
                projections { rowCount() }
            }
        }

        def fields = funcionarioInstanceList.collect { f ->
            [
                    id       : f.id,
                    matricula: f.matricula,
                    nome     : f.nome,
                    cartao   : f?.portador?.cartaoAtivo?.numeroMascarado ?: '< Nenhum cartão ativo >',
                    cpf      : "<a href=${createLink(action: 'show', id: f.id)}>${f.cpf}</a>"
            ]

        }

        def data = [recordsTotal: funcionarioInstanceTotal, results: fields.sort { it.nome }]

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

    def search = {
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
