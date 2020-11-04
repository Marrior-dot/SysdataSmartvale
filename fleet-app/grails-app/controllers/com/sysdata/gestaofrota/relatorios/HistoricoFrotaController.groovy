package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.TipoTransacao
import com.sysdata.gestaofrota.Transacao
import com.sysdata.gestaofrota.TransacaoService
import com.sysdata.gestaofrota.Util
import grails.core.GrailsApplication

class HistoricoFrotaController {


    def exportService

    GrailsApplication grailsApplicationS

    HistoricoFrotaService  historicoFrotaService


    def index() {


        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0


        final Map filtro = [
                dataInicio           : params.date('dataInicio', 'dd/MM/yyyy'),
                dataFim              : params.date('dataFim', 'dd/MM/yyyy')?.plus(1),
              /*  numeroCartao         : params.numeroCartao,
                codigoEstabelecimento: params.codigoEstabelecimento,
                nsu                  : params.int('nsu'),
                tipo                 : params.tipo ? TipoTransacao.valueOf(params.tipo.toString()) : null,
                tipos                : [
                        TipoTransacao.CONSULTA_PRECOS,
                        TipoTransacao.CONFIGURACAO_PRECO,
                        TipoTransacao.CARGA_SALDO,
                        TipoTransacao.TRANSFERENCIA_SALDO
               ] */
        ]



        if (params?.f && params.f != "html") {
            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=historicoFrota.${params.extension}")
/*

              def histFrotaReport = historicoFrotaService.list(params, false)
            histFrotaReport = histFrotaReport.collect {
                  [
                          "nsu": "nsu",
                          "dateCreated": "dateCreated",
                          "dateCreated": "dateCreated",
                          "maquina.placa": "maquina.placa" ,
                          "maquina.marca":"maquina.marca",
                          "maquina.modelo": "maquina.modelo",
                          "maquina.codigo":  "maquina.codigo",
                          "maquina.tipo": "maquina.tipo",
                          "numeroCartao":  "numeroCartao",
                          "participante.nome":  "participante.nome",
                          "produtos.produto.nome": "produtos.produto.nome",
                       // "precoUnitario": Util.formatCurrency("precoUnitario"),
                       // "valor":  Util.formatCurrency("valor"),
                          "precoUnitario":"precoUnitario",
                          "valor":"valor",
                          "qtd_litros": "qtd_litros",
                          "cartao.portador.unidade.rh.nomeFantasia":"cartao.portador.unidade.rh.nomeFantasia",
                          "cartao.portador.unidade.nome":  "cartao.portador.unidade.nome",
                          "tipo":  "tipo",
                          "statusControle":  "statusControle"
                  ]
              } */


          //  def placa = { _, value ->
         //       return value?.maquina.placa + " "
        //    }



           // def date = {_,value -> value?.format('hh:MM:ss')}


            List fields = ["nsu","dateCreated","terminal","estabelecimento.nomeFantasia","cartao.portador.unidade.nome","estabelecimento.cnpj","maquina.placa","maquina.marca",
                           "maquina.modelo", "quilometragem", "maquina.codigo","maquina.tipo","numeroCartao","cartao.portador.unidade.rh.vinculoCartao",
                           "participante.nome","participante.matricula","produtos.produto.nome", "precoUnitario","valor", "qtd_litros","cartao.portador.unidade.nome",
                           "tipo","statusControle" ]

            Map labels = [ "nsu": "Nsu","dateCreated": "Data","terminal": "Terminal", "estabelecimento.nomeFantasia": "Estabelecimento","cartao.portador.unidade.nome": "Unidade", "estabelecimento.cnpj": "CNPJ",
                           "maquina.placa": "Placa","maquina.marca": "Marca","maquina.modelo":"Modelo","quilometragem": "Hodometro",
                           "maquina.codigo": "Cod Equipamento", "maquina.tipo": "Tipo Equipamento","numeroCartao": "Cartão",
                           "cartao.portador.unidade.rh.vinculoCartao": "Vinculo Cartão", "participante.nome": "Funcionario",
                           "participante.matricula": "Matricula", "produtos.produto.nome": "Produto","precoUnitario": "Preço Litro", "valor": "Valor","qtd_litros": "Qtd Litros",
                           "tipo": "Tipo","statusControle": "Status" ]

             Map formatters = [      ]
               Map parameters = [title: "Historico de Frota", "column.widths": [0.2, 0.3, 0.5]]

         //  def data  = { _, value ->
              //  return _?.maquina.placa + "---- "

         /*   Map formatters = [
                           //  maquina.placa data
                         //  dateCreated :  dateCreated.format('dd/MM/yyyy HH:mm:ss')
                           dataInicio : params.date('dataInicio', 'dd/MM/yyyy'),
                           dataFim   : params.date('dataFim', 'dd/MM/yyyy')?.plus(1)

                                 ] */

            exportService.export(params.f,
                    response.outputStream,
                    historicoFrotaService.list(params, false),
                    fields,
                    labels, [:], [:])

            return

        }

        [historicoFrotaList: historicoFrotaService.list(params), historicoFrotaCount: historicoFrotaService.count(params)]
    }


}

