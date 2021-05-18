package com.sysdata.gestaofrota

import grails.converters.JSON
import grails.core.GrailsApplication
import grails.validation.ValidationException
import org.springframework.http.HttpStatus

//@Secured(['IS_AUTHENTICATED_FULLY'])
class EquipamentoController extends BaseOwnerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    EquipamentoService equipamentoService
    def processamentoService
    def cartaoService
    def portadorService

    GrailsApplication grailsApplication

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {

    }

    def create() {
        Unidade unidade = Unidade.get(params.long('unidade.id'))
        def ret = equipamentoService.create(unidade, params)
        if (ret.success) {
            render(view: "form", model: [equipamentoInstance: ret.equipamento,
                                         action: Util.ACTION_NEW,
                                         tamMaxEmbossing: grailsApplication.config.projeto.cartao.embossing.maximoColunasLinhaEmbossing])
        } else {
            flash.error = ret.message
            redirect(controller: 'rh', action: 'show', id: unidade?.rh?.id)
            return
        }
    }

    def save() {
        Equipamento equipamentoInstance = new Equipamento(params)

        if (equipamentoInstance) {
            try {
                def ret = equipamentoService.save(equipamentoInstance, true)

                if (ret.success) {
                    flash.message = "${message(code: 'default.created.message', args: [message(code: 'equipamento.label', default: ''), equipamentoInstance.id])}"
                    redirect(action: "show", id: equipamentoInstance.id)
                } else {
                    if (ret.message)
                        flash.error = ret.message
                    render(view: "form", model: [
                                                    equipamentoInstance: equipamentoInstance,
                                                    action: Util.ACTION_NEW,
                                                    tamMaxEmbossing: grailsApplication.config.projeto.cartao.embossing.maximoColunasLinhaEmbossing])
                }
            }
            catch (Exception e) {
                flash.error = "Equipamento não pode ser salvo. Contate suporte."
                e.printStackTrace()
                render(view: "form", model: [equipamentoInstance: equipamentoInstance,
                                             unidadeInstance: equipamentoInstance.unidade,
                                             action: Util.ACTION_NEW,
                                             tamMaxEmbossing: grailsApplication.config.projeto.cartao.embossing.maximoColunasLinhaEmbossing])
            }
        } else {
            flash.error = "Unidade não encontrada"
            redirect(action: 'index')        }
    }

    def show() {
        def equipamentoInstance = Equipamento.get(params.long('id'))
        if (!equipamentoInstance) {
            flash.errors = "${message(code: 'default.not.found.message', args: [message(code: 'equipamento.label', default: ''), params.id])}"
            redirect(action: "list")
            return;
        }

        render(view: 'form', model: [equipamentoInstance: equipamentoInstance,
                                     unidadeInstance: equipamentoInstance.unidade,
                                     action: Util.ACTION_VIEW,
                                     tamMaxEmbossing: grailsApplication.config.projeto.cartao.embossing.maximoColunasLinhaEmbossing])
    }

    def edit() {
        def equipamentoInstance = Equipamento.get(params.id)
        if (!equipamentoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'veiculo.label', default: ''), params.id])}"
            redirect(action: "list")
        } else {
            render(view: 'form', model: [equipamentoInstance: equipamentoInstance,
                                         unidadeInstance: equipamentoInstance.unidade,
                                         action: 'editando',
                                         tamMaxEmbossing: grailsApplication.config.projeto.cartao.embossing.maximoColunasLinhaEmbossing])
        }
    }

    def update() {
        def equipamentoInstance = Equipamento.get(params.long('id'))
        if (equipamentoInstance) {
            try {
                equipamentoService.update(equipamentoInstance, params)
            } catch (ValidationException ev) {
                log.error "Erro ao salvar Equipamento -> $ev"
                render(view: 'form', model: [equipamentoInstance: equipamentoInstance,
                                             unidadeInstance: equipamentoInstance.unidade,
                                             action: 'editando',
                                             tamMaxEmbossing: grailsApplication.config.projeto.cartao.embossing.maximoColunasLinhaEmbossing])
                return
            }
            flash.message = "${message(code: 'default.updated.message', args: [message(code: 'equipamento.label', default: 'Equipamento'), equipamentoInstance.id])}"
            redirect(action: "show", id: equipamentoInstance.id)
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'equipamento.label', default: ''), params.id])}"
            redirect(action: "list")
        }
    }

    def delete() {
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

    def listAllJSON() {
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
