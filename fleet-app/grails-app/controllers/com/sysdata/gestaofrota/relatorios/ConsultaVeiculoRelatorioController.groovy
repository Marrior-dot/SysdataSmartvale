package com.sysdata.gestaofrota.relatorios

class ConsultaVeiculoRelatorioController {


/*
def exportService

GrailsApplication grailsApplicationS

    ConsultaVeiculoService  consultaVeiculoService
*/


def index() {


    params.max = params.max ? params.max as int : 10
    params.offset = params.offset ? params.offset as int : 0


    if (params?.f && params.f != "html") {
        response.contentType = grailsApplication.config.grails.mime.types[params.f]
        response.setHeader("Content-disposition", "attachment; filename=Veiculo.${params.extension}")



        List fields = ["dateCreated","estabelecimento.nome","placa", "cartao.numero", "maquina.hodometro",
                       "produtos.produto.nome","precoUnitario","valor","cartao.portador.unidade.rh.nomeFantasia",
                       "cartao.portador.unidade.nome"]

        Map labels = [ "dateCreated": "Data/hora","estabelecimento.nome": "Estabelecimento",
                       "placa": "Placa","cartao.numero": "Cartão", "maquina.hodometro": "Ultimo Hodomentro",
                      "produtos.produto.nome": "Produto","precoUnitario": "Preço Litro","valor": "Valor",
                      "cartao.portador.unidade.rh.nomeFantasia": "Cliente", "cartao.portador.unidade.nome": "Unidade" ]

        //  Map formatters = [author: upperCase]
        Map parameters = [title: "Base de Veiculos", "column.widths": [0.2, 0.3, 0.5]]

        exportService.export(params.f,
                response.outputStream,
                consultaVeiculoService.list(params, false),
                fields,
                labels, [:], [:])

        return

    }

    [consultaVeiculoList: consultaVeiculoService.list(params), consultaVeiculoCount: consultaVeiculoService.count(params)]
}


}

