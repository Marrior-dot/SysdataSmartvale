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
        } else
            render(view: 'form', model: [rhInstance: rhInstance, action: Util.ACTION_VIEW, roleRh:roleRh, usuario:usuario])

    }

    def save() {
        Rh rhInstance = new Rh(params)

        try {
            def ret = rhService.save(rhInstance, params)
            if (ret.success) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'rh.label', default: 'Programa'), rhInstance.id])}"
                redirect(action: 'show', id: rhInstance.id)
            } else {
                flash.error = ret.message
                render(view: "form", model: [rhInstance: rhInstance, action: Util.ACTION_NEW])
                return
            }
        }
        catch (e) {
            e.printStackTrace()
            flash.error = e.message
            render(view: "form", model: [rhInstance: rhInstance, action: Util.ACTION_NEW])
            return
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
            try {
                def ret = rhService.update(rhInstance, params)
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

        def rhInstanceList = Rh.createCriteria().list() {
                                eq('status', Status.ATIVO)

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


        def rhInstanceTotal = Rh.createCriteria().list() {
                                    eq('status', Status.ATIVO)
                                    if (params.opcao && params.filtro) {


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

        def fields = rhInstanceList.collect { r ->
            [   id      : r.id,
                razao   : r.nome,
                fantasia: r.nomeFantasia,
                modelo  : r.modeloCobranca.nome,
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

    def listEstabVinculados() {
        def estabs = Rh.get(params.prgId as Long).empresas
        render template: 'tabelaEstabVinculados', model: [estabelecimentoInstanceList: estabs]
    }

    def getTaxas() {
        def taxas = [:]
        Rh rh = Rh.get(params.long('rhId'))
        if (rh) {
            taxas.admin = Util.formatPercentage(rh.taxaAdministracao)
            taxas.desc = Util.formatPercentage(rh.taxaDesconto)
        }
        render taxas as JSON

    }

    def listEstabsVinculados() {
        if (params.rhId) {

            params.max = params.max ? params.max as int : 10
            params.offset = params.offset ? params.offset as int : 0

            Rh rh = Rh.get(params.rhId.toLong())
            def ret = rhService.listEstabsVinculados(rh, params)

            render template: 'estabsTable',
                    model: [
                                action: 'show',
                                estabList: ret.estabList,
                                estabCount: ret.estabCount
                            ],
                    params: params
        } else
            render status: 500, text: "ID RH não enviado na requisição"

    }

    def editEstabsVinculados() {

        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0

        Rh rh = Rh.get(params.rhId.toLong())
        def ret = rhService.editEstabsVinculados(params)

        render template: 'estabsTable',
                model: [
                        rhInstance: rh,
                        action: 'edit',
                        estabList: ret.estabList,
                        estabCount: ret.estabCount
                    ],
                params: params
    }

    def saveEstabsVinculados() {

        def pars = request.JSON

        if (pars.rhId) {

            pars.max =  10
            pars.offset = 0

            Rh rh = Rh.get(pars.rhId as long)
            rhService.saveEstabsVinculados(rh, pars)
            flash.message = "Estabelecimentos Vinculados"

            def ret = rhService.listEstabsVinculados(rh, pars)

            render template: 'estabsTable', model: [
                                                    action: 'show',
                                                    estabList: ret.estabList,
                                                    estabCount: ret.estabCount
                                            ],
                    params: pars


        } else
            flash.error = "ID RH não informado na requisição"


    }

}