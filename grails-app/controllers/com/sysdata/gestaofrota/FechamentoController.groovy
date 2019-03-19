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
        println "programa: ${programa}"
        def fechamentoList = Fechamento.ativosPorPrograma(programa).list()
        println "fechamentoList: ${fechamentoList}"
        User usuario = User.get(params.long('usuario'))
        render(template: 'index', model: [fechamentoList: fechamentoList, usuario:usuario])
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

    def abrirCortes(){
        Fechamento fechamento=Fechamento.get(params.long('id'))
        def cortes=fechamento.cortes.sort{it.dataPrevista}
        render template:'cortes',model:[cortes:cortes,prgId:fechamento.programa.id]
    }

    def abrirFechamentos(){
        def fechamentoList = Fechamento.findAllByPrograma(Rh.get(params.long('id')))

        println "fechamentos: ${fechamentoList}"
        render template: 'index', model:[fechamentoList:fechamentoList]
    }

    def downloadBoleto(){
        Corte corte=Corte.get(params.corId as long)
        Rh rh=Rh.get(params.prgId as long)

        def fatura=Fatura.executeQuery("""select f
from Fatura f, Conta c, Participante p
where f.conta=c
and p.conta=c
and p.id=:id
""",[id:rh.id])[0]


        if(fatura.boletos){
            Boleto boleto=fatura.boletos.asList()[0]
            byte[] pdf=boleto.imagem

            response.setContentType("application/pdf")
            response.setHeader("Content-disposition","attachment;filename=boleto_${boleto.titulo}.pdf")
            response.outputStream<<pdf
            response.outputStream.flush()
        }


    }

}
