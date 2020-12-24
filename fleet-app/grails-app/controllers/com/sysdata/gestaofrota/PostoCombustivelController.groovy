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


class PostoCombustivelController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def postoCombustivelService

    def springSecurityService

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def criteria = {
            order('id')
        }
        def postoCombustivelInstanceList = PostoCombustivel.createCriteria().list(params, criteria)
        [postoCombustivelInstanceList: postoCombustivelInstanceList, postoCombustivelInstanceTotal: PostoCombustivel.count()]
    }


    def create() {
        render(view: "form", model: [action: 'novo'])
    }

    def save() {

        PostoCombustivel postoCombustivelInstance = new PostoCombustivel(params)

        if (postoCombustivelInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), postoCombustivelInstance.id])}"
            redirect(action: "show", id: postoCombustivelInstance.id)
        } else {
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
            render(view: "form", model: [postoCombustivelInstance: postoCombustivelInstance, action: 'editando'])
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

    def delete = {
        def postoCombustivelInstance = PostoCombustivel.get(params.id)
        if (postoCombustivelInstance) {
            try {
                log.debug('Inativando ' + postoCombustivelInstance)
                postoCombustivelInstance = PostoCombustivel.get(params.id)
                postoCombustivelInstance.status = Status.INATIVO
                postoCombustivelInstance.estabelecimentos.each {
                    log.debug('Inativando ' + it)
                    it.status = Status.INATIVO
                }
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                log.error("Erro ao deletar posto combustivel " + postoCombustivelInstance)
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'postoCombustivel.label', default: 'PostoCombustivel'), params.id])}"
                redirect(action: "show", params: [selectedId: params.id])
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

    def getIntervalosReembolso() {
        def postoCombustivelInstance = PostoCombustivel.get(params.id)

        def jsonData
        if (postoCombustivelInstance.tipoReembolso == TipoReembolso.INTERVALOS_MULTIPLOS) {

            def user = springSecurityService.currentUser
            def hasPerm = user.authorities.find { it.authority in ['ROLE_PROC', 'ROLE_ADMIN'] }

            def fields = postoCombustivelInstance.reembolsos.collect { r ->

                def map = [
                            inicio       : r.inicioIntervalo,
                            fim          : r.fimIntervalo,
                            diaEfetivacao: r.diaEfetivacao,
                            meses        : r.meses
                        ]

                if (hasPerm)
                    map['acao'] = "&nbsp<a href='#' onclick='openModal(${r.id});'><span class='glyphicon glyphicon-edit'/></a>&nbsp&nbsp&nbsp<a href='#' onclick='deleteReembolso(${r.id});'><span class='glyphicon glyphicon-remove'/></a>"
                else
                    map['acao'] = ""

                return map
            }

            jsonData = [totalRecords: postoCombustivelInstance.reembolsos.size(), results: fields]
        } else
            jsonData = [totalRecords: 0, results: []]

        render jsonData as JSON
    }

    def deleteReembolso() {
        def retorno
        def reembolsoInstance = Reembolso.get(params.id)
        if (reembolsoInstance) {
            try {
                reembolsoInstance.delete(flush: true)
                retorno = [type: "ok", message: "Reembolso excluído"]
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                retorno = [type: "error", message: "Violação de integridade durante exclusão de Reembolso"]
            }
        }
        render retorno as JSON
    }


    def getReembolsoSemanal() {
        def postoCombustivelInstance = PostoCombustivel.get(params.id)

        def jsonData

        if (postoCombustivelInstance.tipoReembolso == TipoReembolso.SEMANAL) {

            def user = springSecurityService.currentUser
            def hasPerm = user.authorities.find { it.authority in ['ROLE_PROC', 'ROLE_ADMIN'] }

            def fields = postoCombustivelInstance.reembolsos.collect { r ->
                def map = [
                            diaSemana    : r.diaSemana.nome,
                            intervaloDias: r.intervaloDias
                        ]
                if (hasPerm)
                    map['acao'] = "&nbsp<a href='#' onclick='openModal(${r.id});'><span class='glyphicon glyphicon-edit'/></a>&nbsp&nbsp&nbsp<a href='#' onclick='deleteReembolso(${r.id});'><span class='glyphicon glyphicon-trash'/></a>"
                else
                    map['acao'] = ""
                return map
            }

            jsonData = [totalRecords: postoCombustivelInstance.reembolsos.size(), results: fields]
        } else
            jsonData = [totalRecords: 0, results: []]


        render jsonData as JSON
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

    def manageReembolsoDias() {
        ReembolsoDias reembolsoDias
        if (params.id && params.id ==~ /\d+/) {
            reembolsoDias = ReembolsoDias.get(params.id as long)
            render(template: 'reembolsoDias', model: [reembolso: reembolsoDias])
            return
        } else {
            if (params.parId && params.parId ==~ /\d+/) {
                PostoCombustivel empresa = PostoCombustivel.get(params.parId as long)
                reembolsoDias = new ReembolsoDias(participante: empresa)
                render(template: 'reembolsoDias', model: [reembolso: reembolsoDias])
            } else
                render status: 500, text: "ID da Empresa não é um número válido (${params.parId})!"

            return
        }
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

    def saveReembolsoDias() {
        ReembolsoDias reembolsoDias

        def op = ""

        def ret = []
        if (params.id && params.id ==~ /\d+/) {
            reembolsoDias = ReembolsoDias.get(params.id.toLong())
            op = "inserido"
        }
        else {
            reembolsoDias = new ReembolsoDias()
            op = "alterado"
        }

        reembolsoDias.properties = params
        if (reembolsoDias.save(flush: true))
            ret = [type: "ok", message: "Reembolso Dias #${reembolsoDias.id} " + op]
        else
            ret = [type: "error", message: reembolsoDias.errors.allErrors*.defaultMessage.join(";")]


        render ret as JSON

    }

}
