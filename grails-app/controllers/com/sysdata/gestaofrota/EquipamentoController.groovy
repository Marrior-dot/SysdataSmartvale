package com.sysdata.gestaofrota

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.springframework.http.HttpStatus

@Secured(['IS_AUTHENTICATED_FULLY'])
class EquipamentoController extends BaseOwnerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def equipamentoService
    def processamentoService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {

    }

    def newList = {}

    def create = {
        Unidade unidade = Unidade.get(params.long('unidade.id'))
        if (unidade) {
            if (TipoEquipamento.count() == 0) {
                flash.error = "É necessário criar primeiramente um Tipo de Equipamento."
                redirect(controller: 'tipoEquipamento', action: 'list')
                return
            } else {
                render(view: "form", model: [unidadeInstance: unidade, action: Util.ACTION_NEW, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
            }
        } else {
            flash.error = "Unidade não encontrada."
            redirect(action: 'selecionarRhUnidade')
        }
    }

    def save = {
        Equipamento equipamentoInstance = new Equipamento(params)
        Unidade unidadeInstance = Unidade.get(params.long('unidId'))

        if (unidadeInstance != null) {
            try {
                equipamentoInstance.unidade = unidadeInstance

                if(unidadeInstance.rh.vinculoCartao==TipoVinculoCartao.MAQUINA){

                    equipamentoInstance.portador = new PortadorMaquina()
                    equipamentoInstance.portador.unidade = unidadeInstance
                    equipamentoInstance.portador.valorLimite = params.double('portador.valorLimite')
                    equipamentoInstance.portador.tipoLimite = TipoLimite.valueOf(params['portador.tipoLimite'])
                    equipamentoInstance = equipamentoService.save(equipamentoInstance)
                    flash.message = "${message(code: 'default.created.message', args: [message(code: 'equipamento.label', default: ''), equipamentoInstance.id])}"
                    redirect(action: "show", id: equipamentoInstance.id)

                }else
                    equipamentoInstance.save flush: true

                flash.message = "${message(code: 'default.created.message', args: [message(code: 'equipamento.label', default: ''), equipamentoInstance.id])}"
                redirect(controller: 'unidade', action: "show", id: unidadeInstance.id)

            }
            catch (Exception e) {
                e.printStackTrace()
                flash.error = "Erros encontrados."
                render(view: "form", model: [equipamentoInstance: equipamentoInstance, unidadeInstance: equipamentoInstance.unidade, action: Util.ACTION_NEW, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
            }
        } else {
            flash.error = "Unidade não encontrada."
            render(view: "form", model: [equipamentoInstance: equipamentoInstance, unidadeInstance: equipamentoInstance.unidade, action: Util.ACTION_NEW, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
        }
    }

    def show = {
        def equipamentoInstance = Equipamento.get(params.long('id'))
        if (!equipamentoInstance) {
            flash.errors = "${message(code: 'default.not.found.message', args: [message(code: 'equipamento.label', default: ''), params.id])}"
            redirect(action: "list")
            return;
        }

        render(view: 'form', model: [equipamentoInstance: equipamentoInstance, unidadeInstance: equipamentoInstance.unidade, action: Util.ACTION_VIEW, tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
    }

    def edit = {
        def equipamentoInstance = Equipamento.get(params.id)
        if (!equipamentoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'veiculo.label', default: ''), params.id])}"
            redirect(action: "list")
        } else {
            render(view: 'form', model: [equipamentoInstance: equipamentoInstance, unidadeInstance: equipamentoInstance.unidade, action: 'editando', tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
        }
    }

    def update = {
        def equipamentoInstance = Equipamento.get(params.long('id'))
        if (equipamentoInstance) {
            long version = params.long('version')
            if (version != null && equipamentoInstance.version > version) {
                equipamentoInstance.errors.rejectValue("version", "default.optimistic.locking.failure", "Outro usuário estava editando esse Equipamento.")
                render(view: 'form', model: [equipamentoInstance: equipamentoInstance, unidadeInstance: equipamentoInstance.unidade, action: 'editando', tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
                return
            }
            equipamentoInstance.properties = params
            equipamentoInstance.portador.valorLimite = params.double('portador.valorLimite')
            equipamentoInstance.portador.tipoLimite = TipoLimite.valueOf(params['portador.tipoLimite'])
            if (!equipamentoInstance.hasErrors() && equipamentoInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'equipamento.label', default: 'Equipamento'), equipamentoInstance.id])}"
                redirect(action: "show", id: equipamentoInstance.id)
            } else {
                render(view: 'form', model: [equipamentoInstance: equipamentoInstance, unidadeInstance: equipamentoInstance.unidade, action: 'editando', tamMaxEmbossing: processamentoService.getEmbossadora().getTamanhoMaximoNomeTitular()])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'equipamento.label', default: ''), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def equipamentoInstance = Equipamento.get(params.id)
        if (equipamentoInstance) {
            try {
                equipamentoInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'equipamento.label', default: 'Equipamento'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'equipamento.label', default: 'Equipamento'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'equipamento.label', default: 'Equipamento'), params.id])}"
            redirect(action: "list")
        }
    }

    def listAllJSON = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
//        def offset = params.start ?: 10
        def opcao
        def filtro
        def unidId = params.unidade_id ? (params.unidade_id != 'null' ? params.unidade_id.toLong() : null) : null

        def equipInstanceList

        withSecurity { ownerList ->
            equipInstanceList = Equipamento
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
                    //Código
                    if (opcao == 1)
                        like('codigo', filtro + '%')
                    //Descrição
                    else if (opcao == 2)
                        like('descricao', filtro + '%')
                }
            }
        }

        def equipInstanceTotal

        withSecurity { ownerList ->
            equipInstanceTotal = Equipamento.createCriteria().list() {

                if (ownerList.size > 0)
                    unidade { rh { 'in'('id', ownerList) } }

                if (unidId)
                    unidade { eq('id', unidId) }
                if (params.opcao && params.filtro) {
                    //Código
                    if (opcao == 1)
                        like('codigo', filtro + '%')
                    //Marca
                    else if (opcao == 2)
                        like('descricao', filtro + '%')
                }
                projections { rowCount() }
            }
        }


        def fields = equipInstanceList.collect { e ->
            [
                    id       : e.id,
                    codigo   : "<a href=${createLink(action: 'show')}/${e.id}>${e.codigo}</a>",
                    descricao: e.descricao,
                    tipo     : e.tipo?.nome,
                    cartao   : e?.portador?.cartaoAtivo?.numeroMascarado ?: '< Nenhum cartão ativo >',
            ]
        }

        def data = [recordsTotal: equipInstanceTotal, results: fields]
        render data as JSON
    }

    def selecionarRhUnidade() {
        [rhInstanceList: Rh.list()]
    }
}
