package com.sysdata.gestaofrota.relatorios

import grails.core.GrailsApplication

class BaseEquipamentosRelatorioController {


def exportService

GrailsApplication grailsApplicationS

    BaseEquipamentosService  baseEquipamentosService




def index() {


    params.max = params.max ? params.max as int : 10
    params.offset = params.offset ? params.offset as int : 0


    if (params?.f && params.f != "html") {
        response.contentType = grailsApplication.config.grails.mime.types[params.f]
        response.setHeader("Content-disposition", "attachment; filename=baseEquipamentos.${params.extension}")



        List fields = ["unidade.rh.nomeFantasia","unidade.nome","codigo", "tipo", "mediaConsumo",
                       "capacidadeTanque","tipoAbastecimento"]

        Map labels = [ "unidade.rh.nomeFantasia": "Cliente","unidade.nome": "Unidade",
                       "codigo": "Codigo","tipo": "Tipo", "mediaConsumo": "Media de Consumo",
                       "capacidadeTanque": "Cap Tanque (lts)","tipoAbastecimento": "Tipo Combustivel" ]

        //  Map formatters = [author: upperCase]
        Map parameters = [title: "Base de Equipamentos", "column.widths": [0.2, 0.3, 0.5]]

        exportService.export(params.f,
                response.outputStream,
                baseEquipamentosService.list(params, false),
                fields,
                labels, [:], [:])

        return

    }

    [baseEquipamentosList: baseEquipamentosService.list(params), baseEquipamentosCount: baseEquipamentosService.count(params)]
}


}

