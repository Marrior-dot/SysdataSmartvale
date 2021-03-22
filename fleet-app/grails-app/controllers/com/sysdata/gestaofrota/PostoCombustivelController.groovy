package com.sysdata.gestaofrota

import grails.converters.JSON


class IntervaloReembolsoCommand {
    Integer id
    Integer parId
    Integer inicioIntervalo
    Integer fimIntervalo
    Integer diaEfetivacao
    Integer meses
}


class ReembolsoSemanalCommand {
    Integer id
    Integer parId
    DiaSemana diaSemana
    Integer intervaloDias
}

class ReembolsoDiasFixosCommand {
    Integer id
    Integer parId
    Integer diasTranscorridos
}


class PostoCombustivelController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def postoCombustivelService

    def springSecurityService

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        params.offset = params.offset ? params.offset as int: 0
        params.sort = "dateCreated"
        params.order = "desc"
        [empresasList: PostoCombustivel.list(params), empresasCount: PostoCombustivel.count()]
    }


    def create() {
        render(view: "form", model: [action: 'novo'])
    }

    def save() {
        PostoCombustivel postoCombustivelInstance = new PostoCombustivel(params)
        try {
            if (postoCombustivelInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), postoCombustivelInstance.id])}"
                redirect(action: "show", id: postoCombustivelInstance.id)
            } else {
                render(view: "form", model: [postoCombustivelInstance: postoCombustivelInstance, action: Util.ACTION_NEW])
            }
        } catch (e) {
            flash.error = e.message
            render(view: "form", model: [postoCombustivelInstance: postoCombustivelInstance, action: Util.ACTION_NEW])
        }
    }

    def show() {
        def postoCombustivelInstance = PostoCombustivel.get(params.long('id'))
        if (!postoCombustivelInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), params.id])}"
            redirect(action: "list")
        } else {
            render(view: 'form', model: [postoCombustivelInstance: postoCombustivelInstance, action: 'visualizando'])
        }
    }

    def edit() {
        def postoCombustivelInstance = PostoCombustivel.get(params.long('id'))
        if (!postoCombustivelInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), params.id])}"
            redirect(action: "list")
        } else {
            render(view: "form",
                                model: [postoCombustivelInstance: postoCombustivelInstance,
                                        action: 'editando',
                                        editable: true
                                    ])
        }
    }

    def update() {

        PostoCombustivel postoCombustivelInstance = PostoCombustivel.get(params.id as long)
        postoCombustivelInstance.properties = params

        if (postoCombustivelInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.updated.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), postoCombustivelInstance.id])}"
            redirect(action: "show", id: postoCombustivelInstance.id)
        } else {
            render(view: "form", model: [postoCombustivelInstance: postoCombustivelInstance, action: 'editando'])
        }
    }

    def delete() {
        def postoCombustivelInstance = PostoCombustivel.get(params.id)
        if (postoCombustivelInstance) {
            try {

                def ret = postoCombustivelService.delete(postoCombustivelInstance)
                if (ret.success) {
                    flash.success = ret.message
                    log.info ret.message
                }
                else {
                    flash.error = ret.message
                    log.error ret.message
                }
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                log.error("Erro ao deletar posto combustivel " + postoCombustivelInstance)
                flash.error = "Erro ao remover credenciado. Contate suporte."
                redirect(action: "show", id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), params.id])}"
            redirect(action: "list")
        }
    }


    def listAllJSON() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def offset = params.offset ?: 10
        def opcao
        def filtro
        def prg = Rh.get(params.prgId as Long)

        def postoCombustivelInstanceList = PostoCombustivel
                .createCriteria()
                .list() {
            //eq('status', Status.ATIVO)
            if (params.opcao && params.filtro) {
                opcao = params.opcao.toInteger()
                filtro = params.filtro
                //Nome Fantasia
                if (opcao == 1)
                    like('nomeFantasia', filtro + '%')
                //CNPJ
                else if (opcao == 2)
                    like('cnpj', filtro + '%')
                //Cod Estab
                else if (opcao == 3)
                    estabelecimentos { like('codigo', filtro + '%') }
            }
            def userInstance = getAuthenticatedUser()
            if (userInstance.owner instanceof PostoCombustivel) {
                eq('id', userInstance.owner.id)
            }

        }
        def postoCombustivelInstanceTotal = PostoCombustivel
                .createCriteria()
                .list() {
            eq('status', Status.ATIVO)
            if (params.opcao && params.filtro) {
                //Nome Fantasia
                if (opcao == 1)
                    like('nomeFantasia', filtro + '%')
                //CNPJ
                else if (opcao == 2)
                    like('cnpj', filtro + '%')
                //Cod Estab
                else if (opcao == 3)
                    estabelecimentos { like('codigo', filtro + '%') }
            }
            projections { rowCount() }
        }

        def fields = postoCombustivelInstanceList.collect { p ->
            [id          : p.id,
             sel         : p.vinculado(prg) ? "<input type='checkbox' class='enable target' name='sel' id='sel${p.id}' checked>" : "<input type='checkbox' class='enable target' name='sel' id='sel${p.id}'>",
             razao       : p.nome,
             nomeFantasia: p.nomeFantasia,
             cnpj        : "<a href='${createLink(action: 'show', id: p.id)}'>${p.cnpj}</a>"]
        }

        def data = [totalRecords: postoCombustivelInstanceTotal, results: fields]
        render data as JSON
    }


    def deleteReembolso() {
        def retorno
        def reembolsoInstance = Reembolso.get(params.id)
        if (reembolsoInstance) {
            try {
                reembolsoInstance.delete(flush: true)
                retorno = [type: "ok", message: "Reembolso excluído", hasReemb: (reembolsoInstance.participante as PostoCombustivel).reembolsos.size() > 0]
                render retorno as JSON
                return
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                retorno = [type: "error", message: "Violação de integridade durante exclusão de Reembolso"]
            }
        } else {
            retorno = [type: "error", message: "Reembolso #${params.id} não encontrado"]
            render status: 500, text: retorno as JSON
            return
        }
    }

    def manageReembolso() {
        def reembolsoCommand

        def reembolsoInstance = Reembolso.get(params.id)
        if (reembolsoInstance) {
            reembolsoCommand = new IntervaloReembolsoCommand(id: reembolsoInstance.id,
                    parId: reembolsoInstance.participante.id,
                    inicioIntervalo: reembolsoInstance.inicioIntervalo,
                    fimIntervalo: reembolsoInstance.fimIntervalo,
                    diaEfetivacao: reembolsoInstance.diaEfetivacao,
                    meses: reembolsoInstance.meses)
        } else
            reembolsoCommand = new IntervaloReembolsoCommand(parId: params.parId.toInteger())
        render(template: 'reembolso', model: [reembolsoInstance: reembolsoCommand])
    }


    def manageReembolsoSemanal() {
        def reembolsoCommand

        def reembolsoInstance = Reembolso.get(params.id)
        if (reembolsoInstance) {
            reembolsoCommand = new ReembolsoSemanalCommand(id: reembolsoInstance.id,
                    parId: reembolsoInstance.participante.id,
                    diaSemana: reembolsoInstance.diaSemana,
                    intervaloDias: reembolsoInstance.intervaloDias)
        } else
            reembolsoCommand = new ReembolsoSemanalCommand(parId: params.parId.toInteger())

        render(template: 'reembolsoSemanal', model: [reembolsoInstance: reembolsoCommand])
    }

    def manageReembolsoDias() {
        ReembolsoDias reembolsoDias

        if (params.id && params.id ==~ /\d+/) {
            reembolsoDias = ReembolsoDias.get(params.id as long)

            if (reembolsoDias) {
                def reembCommand = new ReembolsoDiasFixosCommand()
                reembCommand.properties = reembolsoDias.properties
                render(template: 'reembolsoDias', model: [reembolso: reembCommand])
                return
            } else {
                render status: 404, text: "Reembolso #${params.id} não encontrado!"
                return
            }

        } else {
            if (params.parId && params.parId ==~ /\d+/) {
                PostoCombustivel empresa = PostoCombustivel.get(params.parId as long)
                if (empresa) {
                    def reembCommand = new ReembolsoDiasFixosCommand(parId: empresa.id)
                    render(template: 'reembolsoDias', model: [reembolso: reembCommand])
                    return
                } else {
                    render status: 404, text: "Empresa #${params.parId} não encontrada!"
                    return
                }
            } else {
                render status: 500, text: "Request Params inválidos!"
                return
            }
        }
    }



    def saveReembolsoSemanal(ReembolsoSemanalCommand cmd) {

        def postoCombustivelInstance = PostoCombustivel.get(cmd.parId)
        def reembolsoInstance = ReembolsoSemanal.get(cmd.id)

        def retorno

        if (!reembolsoInstance) {

            if (postoCombustivelInstance.reembolsos.size() == 0) {
                postoCombustivelInstance.addToReembolsos(new ReembolsoSemanal(cmd.properties))
                postoCombustivelInstance.tipoReembolso = TipoReembolso.SEMANAL

                if (postoCombustivelInstance.save(flush: true))
                    retorno = [type: "ok", message: "Reembolso inserido"]
                else {
                    retorno = [type: "error", message: "Reembolso Não Inserido"]
                    postoCombustivelInstance.errors.allErrors.each {
                        log.error it
                    }
                }
            } else {
                retorno = [type: "error", message: "Reembolso inválido! Já existe(m) reembolso(s) definido(s)"]
            }

        } else {
            reembolsoInstance.properties = cmd.properties
            reembolsoInstance.save(flush: true)
            retorno = [type: "ok", message: "Reembolso alterado"]
        }
        render retorno as JSON

    }


    def saveReembolso(IntervaloReembolsoCommand cmd) {

        def postoCombustivelInstance = PostoCombustivel.get(cmd.parId)
        def reembolsoInstance = ReembolsoIntervalo.get(cmd.id)

        def inicioIntervalo = cmd.inicioIntervalo
        def fimIntervalo = cmd.fimIntervalo
        def diaEfetivacao = cmd.diaEfetivacao
        def meses = cmd.meses

        def valido = true

        def retorno

        def listReembolsos = postoCombustivelInstance.reembolsos
        if (!postoCombustivelInstance.tipoReembolso ||
                postoCombustivelInstance.tipoReembolso == TipoReembolso.INTERVALOS_MULTIPLOS ||
                    listReembolsos.size() == 0) {

            if (inicioIntervalo <= fimIntervalo) {

                def reembolsos = listReembolsos.findAll {
                    reembolsoInstance ? it.id != reembolsoInstance.id : true
                }.sort { it.inicioIntervalo }
                reembolsos.each { r ->
                    if (inicioIntervalo >= r.inicioIntervalo && inicioIntervalo <= r.fimIntervalo) {
                        valido = false
                        return
                    }
                }
                if (valido) {
                    if (!reembolsoInstance) {
                        postoCombustivelInstance.addToReembolsos(new ReembolsoIntervalo(cmd.properties))
                        postoCombustivelInstance.tipoReembolso = TipoReembolso.INTERVALOS_MULTIPLOS
                        postoCombustivelInstance.save(flush: true)
                        retorno = [type: "ok", message: "Reembolso inserido"]
                    } else {
                        reembolsoInstance.properties = cmd.properties
                        reembolsoInstance.save(flush: true)
                        retorno = [type: "ok", message: "Reembolso alterado"]
                    }
                    render retorno as JSON
                } else {
                    retorno = [type: "error", message: "Intervalo inválido"]
                    render retorno as JSON
                }
            } else
                retorno = [type: "error", message: "Início de intervalo superior ao Fim de Intervalo"]
        } else
            retorno = [type: "error", message: "Reembolso inválido! Já existe(m) reembolso(s) definido(s)"]

        render retorno as JSON

    }


    def saveReembolsoDias(ReembolsoDiasFixosCommand reembolsoDiasFixosCommand) {

        def ret = []


        if (reembolsoDiasFixosCommand.parId) {

            PostoCombustivel postoCombustivel = PostoCombustivel.get(reembolsoDiasFixosCommand.parId as long)

            if (postoCombustivel) {
                ReembolsoDias reembolsoDias


                if (postoCombustivel.reembolsos.size() == 0) {
                    def op = ""
                    if (reembolsoDiasFixosCommand.id) {
                        reembolsoDias = ReembolsoDias.get(params.id.toLong())
                        op = "alterado"
                    }
                    else {
                        reembolsoDias = new ReembolsoDias()
                        op = "inserido"
                    }

                    reembolsoDias.properties = reembolsoDiasFixosCommand.properties

                    postoCombustivel.tipoReembolso = TipoReembolso.DIAS_FIXOS
                    postoCombustivel.addToReembolsos(reembolsoDias)

                    //if (reembolsoDias.save(flush: true))
                    if (postoCombustivel.save(flush: true))
                        ret = [type: "ok", message: "Reembolso Dias #${reembolsoDias.id} " + op]
                    else
                        ret = [type: "error", message: reembolsoDias.errors.allErrors*.defaultMessage.join(";")]

                    render ret as JSON
                    return

                } else {
                    ret = [type: "error", message: "Empresa já possui reembolso configurado"]
                    render ret as JSON
                    return
                }

            } else {
                ret = [type: "error", message: "Empresa com ID #${params.parId} não encontrada"]
                render ret as JSON
                return
            }
        } else {
            ret = [type: "error", message: "ID de Empresa inválido: ${params.parId}!"]
            render ret as JSON
            return
        }
    }

    def loadReembolsoDias() {
        if (params.id && params.id ==~ /\d+/) {
            PostoCombustivel empresa = PostoCombustivel.get(params.id as long)
            if (empresa) {
                render template: 'listReembolsoDias', model: [empresa: empresa]
                return
            } else {
                render status: 404, text: "Reembolso Dias com id #${params.id} não encontrado!"
                return
            }
        } else
            render status: 500, text: "Reembolso Dias ID não é um número válido (${params.id})!"
    }

    def loadReembolsoIntervalos() {
        if (params.id && params.id ==~ /\d+/) {
            PostoCombustivel empresa = PostoCombustivel.get(params.id as long)
            if (empresa) {
                render template: 'listReembolsoIntervalo', model: [empresa: empresa]
                return
            } else {
                render status: 404, text: "Reembolso Intervalos com id #${params.id} não encontrado!"
                return
            }
        } else
            render status: 500, text: "Reembolso Intervalos ID não é um número válido (${params.id})!"
    }


    def loadReembolsoSemanal() {

        if (params.id && params.id ==~ /\d+/) {
            PostoCombustivel empresa = PostoCombustivel.get(params.id as long)
            if (empresa) {
                render template: 'listReembolsoSemanal', model: [empresa: empresa]
                return
            } else {
                render status: 404, text: "Reembolso Semanal com id #${params.id} não encontrado!"
                return
            }
        } else
            render status: 500, text: "Reembolso Semanal ID não é um número válido (${params.id})!"
    }


}
