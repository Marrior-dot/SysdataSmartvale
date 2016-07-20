package com.sysdata.gestaofrota

import grails.converters.JSON
import grails.plugins.springsecurity.Secured;


@Secured(["ROLE_PROC", "ROLE_ADMIN"])
class UserController extends BaseOwnerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {}

    def listAllJSON() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        def userInstanceList
        def userInstanceTotal

        def option

        withSecurity { ownerList ->
            userInstanceList = User.withCriteria {

                if (params.opcao && params.filtro) {

                    option = params.opcao as int

                    if (option == 1)
                        like("name", params.filtro + "%")
                    if (option == 2)
                        like("username", params.filtro + "%")
                }

                if (ownerList.size > 0)
                    owner { 'in'('id', ownerList) }
                maxResults(params.max)
                firstResult(params.offset ? params.offset as int : 0)

            }
        }

        withSecurity { ownerList ->
            userInstanceTotal = User.withCriteria(uniqueResult: true) {

                if (params.opcao && params.filtro) {
                    if (option == 1)
                        like("name", params.filtro + "%")
                    if (option == 2)
                        like("username", params.filtro + "%")
                }

                if (ownerList.size > 0)
                    owner { 'in'('id', ownerList) }
                projections {
                    rowCount()
                }
            }

        }

        def resultList = userInstanceList.collect { u ->
            [
                    id    : u.id,
                    login : u.username,
                    name  : u.name,
                    roles : u.getAuthorities()*.authority,
                    owner : u.owner.nome,
                    action: "<a class='show' href=${createLink(action: 'show')}/${u.id}></a>"
            ]
        }

        def resultJSON = [totalRecords: userInstanceTotal, results: resultList]

        response.setHeader("Cache-Control", "no-store")

        render resultJSON as JSON

    }

    def create = {
        def userInstance = new User()
        userInstance.properties = params

        render(view: 'form', model: [userInstance: userInstance, action: Util.ACTION_NEW, ownerList: listOwners()])

    }

    def save = {

        def userInstance = new User(params)

        userInstance.enabled = true

        if (params.password == params.confirmPassword) {

            def roleInstance = Role.get(params.role)
            if (roleInstance) {
                if (userInstance.save(flush: true)) {

                    UserRole.create userInstance, Role.get(params.role)
                    flash.message = "${message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), userInstance.id])}"
                    redirect(action: "show", id: userInstance.id)
                } else
                    render(view: "form", model: [userInstance: userInstance, action: Util.ACTION_NEW, ownerList: listOwners()])
            } else {
                flash.message = "Papel não definido para usuário"
                render view: "form", model: [userInstance: userInstance, action: Util.ACTION_NEW, ownerList: listOwners()]
            }

        } else {
            flash.message = "Confirmação não confere com Senha informada"
            render(view: "form", model: [userInstance: userInstance, action: Util.ACTION_NEW, ownerList: listOwners()])
        }

    }

    def show = {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "list")
            return;
        }

        def userRole = UserRole.findByUser(userInstance)
        render(view: 'form', model: [userInstance: userInstance, role: userRole?.role, action: Util.ACTION_VIEW, ownerList: listOwners()])
    }

    def edit = {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "list")
        } else {
            def userRole = UserRole.findByUser(userInstance)
            render(view: "form", model: [userInstance: userInstance, role: userRole?.role, action: Util.ACTION_EDIT, ownerList: listOwners()])
        }
    }

    def update = {
        def userInstance = User.get(params.id)
        if (userInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (userInstance.version > version) {

                    userInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'user.label', default: 'User')] as Object[], "Another user has updated this User while you were editing")
                    render(view: "edit", model: [userInstance: userInstance, ownerList: listOwners()])
                    return
                }
            }

            def userRole = UserRole.findByUser(userInstance)
            userRole?.delete(flush: true)
            UserRole.create userInstance, Role.get(params.role)

            userInstance.properties = params
            if (!userInstance.hasErrors() && userInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'Usuario'), userInstance.id])}"
                redirect(action: "show", id: userInstance.id)
            } else {
                render(view: "edit", model: [userInstance: userInstance, ownerList: listOwners()])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def userInstance = User.get(params.id)
        if (userInstance) {
            try {
                userInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
    }

    def editPassword = {
        def currentUser = springSecurityService.currentUser
        [userInstance: currentUser]
    }

    def saveNewPassword = {
        def userInstance = User.get(params.id)

        def currentPassword = params.currentPassword
        def newPassword = params.newPassword
        def confirmPassword = params.confirmPassword

        if (userInstance.password == springSecurityService.encodePassword(currentPassword)) {
            if (newPassword == confirmPassword) {
                userInstance.password = newPassword
                flash.message = "Senha alterada com sucesso"
                render(view: 'editPassword', model: [userInstance: userInstance])
            } else {
                flash.message = "Confirmação não corresponde a nova senha informada"
                render(view: 'editPassword', model: [userInstance: userInstance])
            }
        } else {
            flash.message = "Senha atual informada não corresponde a do referido usuário"
            render(view: 'editPassword', model: [userInstance: userInstance])
        }
    }

    def enableUser() {
        def userInstance = User.get(params.id)
        if (userInstance)
            userInstance.enabled = true
        redirect(action: "show", id: params.id)
    }

    def unableUser() {
        def userInstance = User.get(params.id)
        if (userInstance)
            userInstance.enabled = false
        redirect(action: "show", id: params.id)
    }

    def listRoles() {
        def roles = Role.withCriteria {
            eq("owner", params.classe)
        }

        render roles as JSON

    }
}
