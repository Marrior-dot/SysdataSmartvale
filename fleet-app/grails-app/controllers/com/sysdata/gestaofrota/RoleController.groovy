package com.sysdata.gestaofrota

import grails.converters.JSON
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class RoleController {

    RoleService roleService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond roleService.list(params), model:[roleCount: roleService.count()]
    }

    def show(Long id) {
        respond roleService.get(id)
    }

    def create() {
        def ownerList = []
        ownerList += Processadora.all
        ownerList += Administradora.all
        ownerList += Rh.all
        ownerList += PostoCombustivel.all

        respond new Role(params), model: [ownerList: ownerList]
    }

    def save() {
        Role role = new Role(params)
        if (role == null) {
            notFound()
            return
        }

        try {
            roleService.save(role)
        } catch (ValidationException e) {
            respond role.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'role.label', default: 'Role'), role.id])
                redirect role
            }
            '*' { respond role, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond roleService.get(id)
    }

    def update(Role role) {
        if (role == null) {
            notFound()
            return
        }

        try {
            roleService.save(role)
        } catch (ValidationException e) {
            respond role.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'role.label', default: 'Role'), role.id])
                redirect role
            }
            '*'{ respond role, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }
        try {
            roleService.delete(id)
        } catch (e) {
            log.error "Erro ao remover Role:\n $e"
            flash.message = "Pap??l n??o pode ser removido, pois j?? possui Usu??rios vinculados"
            redirect action: 'show', id: id
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'role.label', default: 'Role'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'role.label', default: 'Role'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }


}
