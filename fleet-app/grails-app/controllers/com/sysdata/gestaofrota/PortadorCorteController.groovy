package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.proc.faturamento.PortadorCorteService
import grails.converters.JSON

class PortadorCorteController {

    PortadorCorteService portadorCorteService

    private def withPortador = { params, clos ->
        if (params.prtId) {
            Portador portador = Portador.get(params.prtId.toLong())
            if (portador)
                clos.call(portador)
            else {
                render status: 404, text: "Portador #${params.prtId} não encontrado"
                return
            }
        } else {
            render status: 404, text: "ID de Portador não informado"
            return
        }
    }

    def findFaturaAberta() {
        withPortador(params) { portador ->
            render template: '/portadorCorte/fatura', model: [fatura: portadorCorteService.findFaturaAberta(portador)]
            return
        }
    }

    def findUltimaFatura() {
        withPortador(params) { portador ->
            render template: '/portadorCorte/fatura', model: [fatura: portadorCorteService.findUltimaFatura(portador)]
            return
        }
    }

    def listFaturasAnteriores() {
        withPortador(params) { portador ->
            render template: '/portadorCorte/faturasAnteriores', model: [fatura: portadorCorteService.listarFaturasAnteriores(portador)]
            return
        }
    }

    def showPortador() {
        withPortador(params) { portador ->
            if (portador.instanceOf(PortadorFuncionario)) {
                redirect(controller: 'funcionario', action: 'show', id: (portador as PortadorFuncionario).funcionario.id)
                return
            } else if (portador.instanceOf(PortadorMaquina)) {
                redirect(controller: 'veiculo', action: 'show', id: (portador as PortadorMaquina).maquina.id)
                return
            }
        }
    }


}
