package com.sysdata.gestaofrota.relatorios

import grails.core.GrailsApplication

class BaseVeiculosRelatorioController {


def exportService

GrailsApplication grailsApplicationS

    BaseVeiculosService  baseVeiculosService




def index() {


    params.max = params.max ? params.max as int : 10
    params.offset = params.offset ? params.offset as int : 0


    if (params?.f && params.f != "html") {
        response.contentType = grailsApplication.config.grails.mime.types[params.f]
        response.setHeader("Content-disposition", "attachment; filename=baseVeiculos.${params.extension}")



        List fields = ["unidade.rh.nomeFantasia","unidade.nome","placa", "marca", "modelo",
                       "chassi","anoFabricacao","capacidadeTanque","tipoAbastecimento"]

        Map labels = [ "unidade.rh.nomeFantasia": "Cliente","unidade.nome": "Unidade",
                       "placa": "Placa","marca": "Marca", "modelo": "Modelo",
                      "chassi": "Chassi","anoFabricacao": "Ano Fabricação","capacidadeTanque": "Cap Tanque (lts)",
                      "tipoAbastecimento": "Tipo Combustivel" ]

        //  Map formatters = [author: upperCase]
        Map parameters = [title: "Base de Veiculos", "column.widths": [0.2, 0.3, 0.5]]

        exportService.export(params.f,
                response.outputStream,
                baseVeiculosService.list(params, false),
                fields,
                labels, [:], [:])

        return

    }

    [baseVeiculosList: baseVeiculosService.list(params), baseVeiculosCount: baseVeiculosService.count(params)]
}


}

