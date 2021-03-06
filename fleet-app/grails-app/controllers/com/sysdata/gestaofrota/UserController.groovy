package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.BusinessException
import grails.converters.JSON
import grails.validation.ValidationException

class UserController extends BaseOwnerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    UserService userService

    def passwordEncoder


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
                //maxResults(params.max)
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
                    login : "<a href=${createLink(action:'show')}/${u.id}>${u.username}</a>",
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

    def create() {
        def userInstance = new User()
        userInstance.properties = params
        render(view: 'form', model: [userInstance: userInstance, action: Util.ACTION_NEW, ownerList: listOwners()])
    }

    def save() {
        User userInstance = new User(params)

        def ret = userService.saveNew(userInstance, params)
        if (ret.success) {
            flash.message = ret.message
            redirect(action: "show", id: userInstance.id)
        } else {
            flash.message = ret.message
            render(view: "form", model: [userInstance: userInstance, action: Util.ACTION_NEW, ownerList: listOwners()])
        }
    }

    def show() {
        User userInstance = User.get(params.long('id'))
        if (!userInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "list")
            return;
        }


        def userRole = UserRole.findByUser(userInstance)
        render(view: 'form', model: [userInstance: userInstance, role: userRole?.role, action: Util.ACTION_VIEW, ownerList: listOwners()])
    }

    def edit() {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "list")
        } else {
            def userRole = UserRole.findByUser(userInstance)
            render(view: "form", model: [
                                            editable: springSecurityService.currentUser.authorities.authority.any { it == 'ROLE_ADMIN' || it == 'ROLE_PROC'},
                                            userInstance: userInstance,
                                            role: userRole?.role,
                                            action: Util.ACTION_EDIT,
                                            ownerList: listOwners()])
        }
    }

    def update() {
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

            try {
                userService.update(userInstance, params)
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'Usuario'), userInstance.id])}"
                redirect(action: "show", id: userInstance.id)

            } catch (ValidationException ve) {
                flash.error = "Altera????o inv??lida: ${ve.message}"
                userInstance = User.get(params.id)
                render(view: "form", model: [
                        editable: springSecurityService.currentUser.authorities.authority.any { it == 'ROLE_ADMIN' || it == 'ROLE_PROC'},
                        userInstance: userInstance,
                        action: Util.ACTION_EDIT,
                        ownerList: listOwners()])
                return
            }

        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete() {
        def userInstance = User.get(params.id)
        if (userInstance) {
            try {
                userInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.error = "${message(code: 'default.not.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
    }

    def editPassword() {
        def currentUser = springSecurityService.currentUser
        [userInstance: currentUser]
    }

    def saveNewPassword() {
        def userInstance = User.get(params.id)
        def currentPassword = params.currentPassword
        def newPassword = params.newPassword
        def confirmPassword = params.confirmPassword

        if (passwordEncoder.isPasswordValid(userInstance.password, currentPassword, null)) {
            try {
                userService.saveNewPassword(userInstance, newPassword, confirmPassword)
                flash.success = "Senha alterada com sucesso"
            } catch (BusinessException be) {
                flash.error = be.message
            }
        } else
            flash.error = "Senha atual informada inv??lida!"

        render(view: 'editPassword', model: [userInstance: userInstance])
    }

    def meuUsuario() {
        User user = User.get(params.id as long)
        redirect(action: 'show', id: user.id)
    }


    def meusDados() {
        User userInstance = User.get(params.long('id'))
        if (!userInstance?.owner) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'participante.label', default: 'Participante'), params.id])}"
            redirect(action: "list")
            return
        }

        if(userInstance.owner.instanceOf(Rh)){
            redirect(controller: 'rh', action: 'show', id: userInstance.owner.id)
            return
        }
        else if(userInstance.owner.instanceOf(PostoCombustivel)){
            redirect(controller: 'postoCombustivel', action: 'show', id: userInstance.owner.id)
            return
        }
        else if(userInstance.owner.instanceOf(Estabelecimento)){
            redirect(controller: 'estabelecimento', action: 'show', id: userInstance.owner.id)
            return
        }
        else if(userInstance.owner.instanceOf(Funcionario)){
            redirect(controller: 'funcionario', action: 'show', id: userInstance.owner.id)
            return
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
