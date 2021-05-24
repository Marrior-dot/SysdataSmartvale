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
        def fechamentoList = Fechamento.ativosPorPrograma(programa).list()
        User usuario = User.get(params.long('usuario'))
        render(template: 'index', model: [fechamentoList: fechamentoList, usuario:usuario])
    }

    def save() {

        Fechamento fechamentoInstance = new Fechamento(params)
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
        def ret= fechamentoService.delete(fechamentoInstance)
        if (ret.success) {
            response.status = HttpStatus.OK.value()
            render (['msg': ret.message] as JSON)
            return
        } else {
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            render text: ret.message
            return
        }
    }

    def abrirCortes(){
        Fechamento fechamento=Fechamento.get(params.long('id'))
        def cortes=fechamento.cortes.sort{it.dataPrevista}
        render template:'cortes',model:[cortes:cortes,prgId:fechamento.programa.id]
    }

    def abrirFechamentos(){
        def fechamentoList = Fechamento.findAllByPrograma(Rh.get(params.long('id')))

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

    def findFaturaByCorte() {
        if (params.id) {
            Corte corte = Corte.get(params.id.toLong())
            if (corte) {
                def fat = fechamentoService.findFaturaByCorte(corte)
                if (fat) {
                    render template: "/portadorCorte/fatura", model: [fatura: fat]
                    return
                } else
                    render status: 404, text: "Não há fatura para o corte #${corte.id}"
            } else
                render status: 404, text: "Corte #${corte.id} não encontrado"
        } else
            render status: 500, text: "Corte ID não informado"
    }


}
