package com.sysdata.gestaofrota

import grails.converters.JSON
import org.springframework.http.HttpStatus

class RhController extends BaseOwnerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def rhService

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def criteria = {
            order('id', 'desc')
        }
        def rhInstanceList = Rh.createCriteria().list(params, criteria)
        [rhInstanceList: rhInstanceList, rhInstanceTotal: Rh.count()]
    }

    def create() {
        render(view: 'form', model: [action: Util.ACTION_NEW, rhInstance: new Rh()])
    }

    def show() {
        Rh rhInstance = Rh.get(params.long('id'))
        User usuario = springSecurityService.getCurrentUser()
        def roleRh
        if(usuario.authorities.authority.contains('ROLE_RH')){
            roleRh = true
        }else{
            roleRh = false
        }

        if (!rhInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'rh.label', default: 'Rh'), params.rhId])}"
            redirect(action: "list")
        } else {
            clearSession()
            render(view: 'form', model: [rhInstance: rhInstance, action: Util.ACTION_VIEW, roleRh:roleRh, usuario:usuario])
        }
    }

    def save(Rh rhInstance) {
        try {
            def ret = rhService.save(rhInstance)
            if (ret.success) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'rh.label', default: 'Programa'), rhInstance.id])}"
                redirect(action: 'show', id: rhInstance.id)
            } else {
                flash.error = ret.message
                render(view: "form", model: [rhInstance: rhInstance, action: Util.ACTION_NEW])
            }
        }
        catch (e) {
            e.printStackTrace()
            flash.error = e.message
            render(view: "form", model: [rhInstance: rhInstance, action: Util.ACTION_NEW])
        }
    }

    def edit() {
        Rh rhInstance = Rh.get(params.long('id'))
        if (!rhInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'rh.label', default: 'Rh'), params.id])}"
            redirect(action: "list")
        } else {
            return render(view: 'form', model: [rhInstance: rhInstance, action: Util.ACTION_EDIT])
        }
    }

    def update() {
        Rh rhInstance = Rh.get(params.long('id'))
        if (rhInstance) {
            rhInstance.properties = params
            try {
                def ret = rhService.update(rhInstance)
                if (ret.success) {
                    flash.message = "${message(code: 'default.updated.message', args: [message(code: 'rh.label', default: 'Programa'), rhInstance.id])}"
                    redirect(action: 'show', id: rhInstance.id)
                } else {
                    flash.error = ret.message
                    render(view: "form", model: [rhInstance: rhInstance, action: Util.ACTION_EDIT])
                }
            } catch (e) {
                e.printStackTrace()
                flash.error = e.message
                render(view: "form", model: [rhInstance: rhInstance, action: Util.ACTION_EDIT])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'rh.label', default: 'Programa'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete() {
        def rhInstance = Rh.get(params.long('id'))
        if (rhInstance) {
            def msg = rhService.delete(rhInstance)
            flash.message = msg
            redirect(action: "list")
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'rh.label', default: 'Rh'), params.id])}"
            redirect(action: "list")
        }
    }

    def autoCompleteJSON() {

        def list
        withSecurity { ownerList ->

            list = Rh.withCriteria {
                eq('status', Status.ATIVO)
                if (ownerList.size > 0)
                    'in'('id', ownerList)

                if (params.query)
                    like("nomeFantasia", params.query + "%")
            }

        }


        def jsonList = list.collect { [id: it.id, name: it.nomeFantasia] }
        def jsonResult = [
                result: jsonList
        ]
        render jsonResult as JSON
    }

    def listAllJSON() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def offset = params.offset ?: 0
        def opcao
        def filtro

        def rhInstanceList

        //withSecurity { ownerList ->
            rhInstanceList = Rh.createCriteria().list() {
                eq('status', Status.ATIVO)

/*
                if (ownerList.size > 0)
                    'in'('id', ownerList)
*/

                if (params.opcao && params.filtro) {
                    opcao = params.opcao.toInteger()
                    filtro = params.filtro
                    //Código
                    if (opcao == 1)
                        eq('codigo', filtro)
                    //Nome Fantasia
                    else if (opcao == 2)
                        like('nomeFantasia', filtro + '%')
                    //CNPJ
                    else if (opcao == 3)
                        like('cnpj', filtro + '%')
                }
            }

        //}

        def rhInstanceTotal

        withSecurity { ownerList ->
            rhInstanceTotal = Rh
                    .createCriteria()
                    .list() {
                eq('status', Status.ATIVO)
                if (params.opcao && params.filtro) {

                    if (ownerList.size > 0)
                        'in'('id', ownerList)

                    //Código
                    if (opcao == 1)
                        eq('codigo', filtro)
                    //Nome Fantasia
                    else if (opcao == 2)
                        like('nomeFantasia', filtro + '%')
                    //CNPJ
                    else if (opcao == 3)
                        like('cnpj', filtro + '%')
                }
                projections { rowCount() }
            }
        }

        def fields = rhInstanceList.collect { r ->
            [   id      : r.id,
                razao   : r.nome,
                fantasia: r.nomeFantasia,
                cnpj    : """<a href='${createLink(action: 'show', id: r.id)}'>${r.cnpj}</a>"""
            ]
        }

        def data = [totalRecords: rhInstanceTotal, results: fields]
        render data as JSON
    }

    def listEmpresasJSON() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def offset = params.offset ? params.int('offset') : 0

        def progInstance = Rh.get(params.prgId)

        def empresaList = PostoCombustivel.findAllByStatus(Status.ATIVO, [max: params.max, offset: offset])

        def resultList = []

        empresaList.each { e ->
            def check = progInstance.empresas.find { it.id == e.id } != null
            resultList << [id: e.id, fantasia: e.nomeFantasia, razao: e.nome, selecao: check]
            fillEstMapOnDemand(e.id, check)

        }


        def jsonList = [totalRecords: PostoCombustivel.count(), results: resultList]

        response.setHeader("Cache-control", "no-store")

        render jsonList as JSON
    }

    def syncOne() {
        def oid = params.oid as long
        def chk = params.chk

        fillEstMapOnDemand(oid, chk)

        render "ok"
    }

    def syncAll() {
        session.selAllEst = params.chk ?: null
        render "ok"

    }

    def saveEstabs() {
        def progInstance = Rh.get(params.prgId)
        log.debug("session:" + session)
        if (progInstance) {

            /* Marca todas as empresas no HTTP Session */
            if (session.selAllEst) {

                PostoCombustivel.all.each {
                    fillEstMapOnDemand(it.id, true)
                }

                session.selAllEst = null
            }


            def mIds = session.mEstIds

            if (mIds) {
                mIds.each { k, v ->
                    def estInstance = progInstance.empresas.find { it.id == k }

                    if (estInstance) {
                        if (v == "false") {
                            progInstance.removeFromEmpresas estInstance
                            println "Removeu Empresa ${estInstance?.id} da relação"
                        }

                    } else {
                        estInstance = PostoCombustivel.get(k)
                        if (v == "true")
                            progInstance.addToEmpresas estInstance
                    }
                }

                if (progInstance.save(flush: true)) {
                    render "ok"
                } else {
                    def data = [type: "error", message: "Erro ao Salvar Programa"]
                    render data as JSON
                }
            } else {
                def data = [type: "error", message: "Não há dados para salvar"]
                render data as JSON
            }
        }
    }

    def listMarkedEstab() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def offset = params.offset ?: 10

        def rh = Rh.get(params.rhId as int)

        def postoCombustivelInstanceList =
                PostoCombustivel.withCriteria {
                    eq('status', Status.ATIVO)
                    def userInstance = getAuthenticatedUser()
                    if (userInstance.owner instanceof PostoCombustivel)
                        eq('id', userInstance.owner.id)
                    maxResults(params.max)
                    firstResult(offset)
                }

        def postoCombustivelInstanceTotal =
                PostoCombustivel.withCriteria {
                    eq('status', Status.ATIVO)
                    projections { rowCount() }
                }



        def listEmp = []

        postoCombustivelInstanceList.each { p ->
            def l = [cnpj: p.cnpj, razao: p.nome, nomeFantasia: p.nomeFantasia]
            if (rh.empresas.find { it == p }) l.sel = true
            else l['sel'] = false
            listEmp << l
        }

        def data = [totalRecords: postoCombustivelInstanceTotal, results: listEmp]
        render data as JSON
    }

    def listEstabNaoVinculados() {
        def estabs = PostoCombustivel.createCriteria().list {
            eq('status', Status.ATIVO)
            programas {
                eq('id', params.prgId as Long)
            }
        }
        def listEstabs = PostoCombustivel.createCriteria().list {
            eq('status', Status.ATIVO)
        }
        def estabsFiltered = listEstabs - estabs
        render estabsFiltered.collect { e -> [id: e.id, nome: e.nomeFantasia + " (" + e.cnpj + ")"] } as JSON
    }

    def salvarEstabelecimentosVinculados() {
        def retorno = [:]
        def estab = PostoCombustivel.get(params.long('selEstId'))
        if (! estab) {
            render status: HttpStatus.NOT_FOUND, text: "Estabelecimento não localizado!"
            return
        }

        def prg = Rh.get(params.prgId as Long)
        prg.addToEmpresas(estab)
        if (prg.save(flush: true, failOnError: true)) {
            retorno.mensagem = "Estabelecimento Adicionado"
        } else {
            retorno.mensagem = "Erro ao Salvar Estabelecimento"
        }
        render retorno as JSON
    }

    def listEstabVinculados() {
        def estabs = Rh.get(params.prgId as Long).empresas
        render template: 'tabelaEstabVinculados', model: [estabelecimentoInstanceList: estabs]
    }

    def desvincularEstab() {
        def retorno = [:]
        def estab = PostoCombustivel.get(params.selectedEstabId as Long)
        def prg = Rh.get(params.prgId as Long)
        prg.removeFromEmpresas(estab)
        if (prg.save(flush: true, failOnError: true)) {
            retorno.mensagem = "Estabelecimento Desvinculado"
        } else {
            retorno.mensagem = "Erro ao desvincular"
        }
        render retorno as JSON
    }

    def getTaxas() {
        def taxas = [:]
        Rh rh = Rh.get(params.long('rhId'))
        if (rh) {
            taxas.taxaPedido = Util.formatPercentage(rh.taxaPedido)
        }
        render taxas as JSON

    }

    private void clearSession() {
        if (session.mEstIds) {
            session.mEstIds = null
            println "Limpou Est IDS da HTTP Session"
        }
    }

    /* Preenche map de estabelecimentos selecionados na HTTP Session conforme demanda
     */

    private void fillEstMapOnDemand(oid, chk) {
        def mIds = session.mEstIds
        if (!mIds) mIds = [:]

        def eid = mIds.find { k, v -> k == oid }
        if (eid) eid.value = chk
        else mIds[oid] = chk

        if (mIds) session.mEstIds = mIds
    }
}