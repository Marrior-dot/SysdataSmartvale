package com.sysdata.gestaofrota

import grails.converters.JSON
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus

import javax.validation.ValidationException

class FechamentoController {

    static allowedMethods = [save: "POST", delete: "DELETE"]
    def fechamentoService

    def index() {
        Rh programa = Rh.get(params.long('programa.id'))
        render(template: 'index', model: [fechamentoList: Fechamento.ativosPorPrograma(programa).list()])
    }

    def save(Fechamento fechamentoInstance) {
        try {
            fechamentoService.save(fechamentoInstance)
            response.status = HttpStatus.OK.value()
            render([msg: 'Fechamento Adicionado'] as JSON)
        }
        catch (ValidationException e){
            response.status = HttpStatus.BAD_REQUEST.value()
            render([erro: e.message] as JSON)
        }
        catch (Exception e){
            e.printStackTrace()
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            render([erro: 'Um erro ocorreu'] as JSON)
        }
    }

    def delete() {
        Fechamento fechamentoInstance = Fechamento.get(params.long('id'))
        fechamentoService.delete(fechamentoInstance)
        response.status = HttpStatus.OK.value()
        render (['msg': 'ok'] as JSON)
    }
}
