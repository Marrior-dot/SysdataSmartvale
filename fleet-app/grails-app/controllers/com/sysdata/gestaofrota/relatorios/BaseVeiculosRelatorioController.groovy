package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Equipamento
import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Unidade
import com.sysdata.gestaofrota.Util
import com.sysdata.gestaofrota.Veiculo
import grails.core.GrailsApplication

class BaseVeiculosRelatorioController {


def exportService

    BaseVeiculosService  baseVeiculosService

def index() {
    params.max = params.max ? params.max as int : 10
    params.offset = params.offset ? params.offset as int : 0


    if (params?.f && params.f != "html") {
        response.contentType = grailsApplication.config.grails.mime.types[params.f]
        response.setHeader("Content-disposition", "attachment; filename=baseVeiculos.${params.extension}")

        def BaseVeiculosRelatorio = []

        def cabecalho = [:]
        cabecalho.empresa = "EMISSAO"
        cabecalho.unidade = new Date().format('dd/MM/yyyy')
        BaseVeiculosRelatorio << cabecalho

        def cabecalho1 = [:]
        if (params.empresa) {
            cabecalho1.empresa = "EMPRESA"
            Rh empresaCliente = Rh.get(params.empresa.toLong())
            cabecalho1.unidade = empresaCliente.nomeFantasia
            BaseVeiculosRelatorio << cabecalho1
        }
        def cabecalho2 = [:]
        if (params.unidade) {
            cabecalho2.empresa = "UNIDADE"
            Unidade unidade = Unidade.get(params.unidade.toLong())
            cabecalho2.unidade = unidade.nome
            BaseVeiculosRelatorio << cabecalho2
        }
        def cabecalho3 = [:]
        if (params.placa) {
            cabecalho3.empresa = "PLACA"
            cabecalho3.unidade = params.placa
            BaseVeiculosRelatorio << cabecalho3
        }
        def cabecalho4 = [:]
        cabecalho4.empresa = ""
        BaseVeiculosRelatorio << cabecalho4

        def cabecalho5 = [:]
        cabecalho5.empresa = "CLIENTE"
        cabecalho5.unidade = "UNIDADE"
        cabecalho5.placa = "PLACA"
        cabecalho5.marcaModelo = "MARCA/MODELO"
        cabecalho5.chassi = "CHASSI"
        cabecalho5.fabricacao = "ANO FABRIC."
        cabecalho5.capTanque = "CAP. TANQUE"
        cabecalho5.tipoCombustivel = "TIPO COMBUST."
        BaseVeiculosRelatorio << cabecalho5

        def reportList = baseVeiculosService.list(params, false)

        //def totalValor = reportList.sum { it.valor }

        reportList = reportList.collect { tr ->
            [
                    "empresa": tr.unidade.rh.nomeFantasia,
                    "unidade": tr.unidade.nome,
                    "placa": tr.placa,
                    "marcaModelo": "${tr.marca} / ${tr.modelo}",
                    "chassi": tr.chassi,
                    "fabricacao": tr.anoFabricacao,
                    "capTanque": tr.capacidadeTanque,
                    "tipoCombustivel": tr.tipoAbastecimento
                    //"tipoCombustivel": "(${tr.participante.matricula}) ${tr.participante.nome}"
            ]
        }

        reportList += [
                "empresa": "",
                "unidade": "",
                "placa": "",
                "marcaModelo": "",
                "chassi": "",
                "fabricacao": "",
                "capTanque": "",
                "tipoCombustivel": ""
        ]

        List fields = [
                "empresa",
                "unidade",
                "placa",
                "marcaModelo",
                "chassi",
                "fabricacao",
                "capTanque",
                "tipoCombustivel"

        ]

        Map labels = [
                "empresa": "CLIENTE",
                "unidade": "UNIDADE",
                "placa": "PLACA",
                "marcaModelo": "MARCA/MODELO",
                "chassi": "CHASSI",
                "fabricacao": "ANO FABRIC.",
                "capTanque": "CAP. TANQUE",
                "tipoCombustivel": "TIPO COMBUST."
        ]

        //  Map formatters = [author: upperCase]
        Map parameters = [title: "Base de Veiculos", "column.widths": [0.2, 0.3, 0.5]]

        exportService.export(params.f,
                response.outputStream,
                BaseVeiculosRelatorio+reportList,
                fields,
                labels, [:],
                ['header.enabled': false])

        return

    }

    [baseVeiculosList: baseVeiculosService.list(params), baseVeiculosCount: baseVeiculosService.count(params)]
}


}

