package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Unidade
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

        def BaseEquipamentosRelatorio = []

        def cabecalho = [:]
        cabecalho.empresa = "EMISSAO"
        cabecalho.unidade = new Date().format('dd/MM/yyyy')
        BaseEquipamentosRelatorio << cabecalho

        def cabecalho1 = [:]
        if (params.empresa) {
            cabecalho1.empresa = "EMPRESA"
            Rh empresaCliente = Rh.get(params.empresa.toLong())
            cabecalho1.unidade = empresaCliente.nomeFantasia
            BaseEquipamentosRelatorio << cabecalho1
        }
        def cabecalho2 = [:]
        if (params.unidade) {
            cabecalho2.empresa = "UNIDADE"
            Unidade unidade = Unidade.get(params.unidade.toLong())
            cabecalho2.unidade = unidade.nome
            BaseEquipamentosRelatorio << cabecalho2
        }
        def cabecalho3 = [:]
        if (params.placa) {
            cabecalho3.empresa = "PLACA"
            cabecalho3.unidade = params.placa
            BaseEquipamentosRelatorio << cabecalho3
        }
        def cabecalho4 = [:]
        cabecalho4.empresa = ""
        BaseEquipamentosRelatorio << cabecalho4

        def cabecalho5 = [:]
        cabecalho5.empresa = "CLIENTE"
        cabecalho5.unidade = "UNIDADE"
        cabecalho5.codigo = "COD. EQUIPAMENTO"
        cabecalho5.tipo = "TIPO"
        cabecalho5.mediaConsumo = "MEDIA CONSUMO"
        cabecalho5.capacidadeTanque = "CAP. TANQUE"
        cabecalho5.tipoAbastecimento = "TIPO ABAST."
        BaseEquipamentosRelatorio << cabecalho5

        def reportList = baseEquipamentosService.list(params, false)

        //def totalValor = reportList.sum { it.valor }

        reportList = reportList.collect { tr ->
            [
                    "empresa": tr.unidade.rh.nomeFantasia,
                    "unidade": tr.unidade.nome,
                    "codigo": tr.codigo,
                    "tipo": tr.tipo,
                    "mediaConsumo": tr.mediaConsumo,
                    "capacidadeTanque": tr.capacidadeTanque,
                    "tipoAbastecimento": tr.tipoAbastecimento
            ]
        }

        reportList += [
                "empresa": "",
                "unidade": "",
                "codigo": "",
                "tipo": "",
                "mediaConsumo": "",
                "capacidadeTanque": "",
                "tipoAbastecimento": ""
        ]

        List fields = ["unidade.rh.nomeFantasia",
                       "unidade.nome",
                       "codigo",
                       "tipo",
                       "mediaConsumo",
                       "capacidadeTanque",
                       "tipoAbastecimento"]

        Map labels = [ "unidade.rh.nomeFantasia": "Cliente",
                       "unidade.nome": "Unidade",
                       "codigo": "Codigo",
                       "tipo": "Tipo",
                       "mediaConsumo": "Media de Consumo",
                       "capacidadeTanque": "Cap Tanque (lts)",
                       "tipoAbastecimento": "Tipo Combustivel" ]

        //  Map formatters = [author: upperCase]
        Map parameters = [title: "Base de Equipamentos", "column.widths": [0.2, 0.3, 0.5]]

        exportService.export(params.f,
                response.outputStream,
                BaseEquipamentosRelatorio+reportList,
                fields,
                labels, [:], ['header.enabled': false])

        return

    }

    [baseEquipamentosList: baseEquipamentosService.list(params), baseEquipamentosCount: baseEquipamentosService.count(params)]
}


}

