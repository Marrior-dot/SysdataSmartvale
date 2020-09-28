package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Veiculo
import grails.core.GrailsApplication

class BaseVeiculosController {


def exportService

GrailsApplication grailsApplicationS

    BaseVeiculosService  baseVeiculosService




def index() {


    params.max = params.max ? params.max as int : 10
    params.offset = params.offset ? params.offset as int : 0


    if (params?.f && params.f != "html") {
        response.contentType = grailsApplication.config.grails.mime.types[params.f]
        response.setHeader("Content-disposition", "attachment; filename=baseVeiculos.${params.extension}")



        List fields = ["placa", "marca", "modelo", "unidade.rh.nomeFantasia", "validadeExtintor","hodometro"]

        Map labels = ["placa": "Placa","marca": "Marca", "modelo": "Modelo",
                      "unidade.rh.nomeFantasia": "Empresa Cliente",
                      "validadeExtintor": "Validade Extintor", "hodometro": "Ult. Hodometro",
                        ]

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

