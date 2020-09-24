package com.sysdata.gestaofrota.relatorios

import grails.core.GrailsApplication
import com.sysdata.gestaofrota.Veiculo

class BaseVeiculosController {

  /*  def exportService

    GrailsApplication grailsApplication

    def index() {

        StringBuilder sb = new StringBuilder()

        sb.append("""
            select
                v.placa,
                v.marca.abreviacao,
                v.modelo,
                v.unidade.rh.nome,
                v.unidade.nome,
                v.anoFabricacao,
                v.hodometro

            from

                Veiculo as v


            """)

        if (params.placa)
            sb.append(" and v.placa = ${params.placa}")

        else if (params.unidade) {
            sb.append(" and v.unidade.id = ${params.unidade as long}")
        }

        if (params.dataInicio && params.dataFim) {
            sb.append(""" and t.dateCreated >= ${params.date('dataInicio', 'dd/MM/yyyy')} and
                t.dateCreated <= ${params.date('dataFim', 'dd/MM/yyyy') + 1} """)
        }

            sb.append("""
            group by
                v.placa,
                v.marca.abreviacao,
                v.modelo,
                v.unidade.rh.nome,
                v.unidade.nome,
                v.anoFabricacao,
                v.hodometro

            order by
                v.placa
            """)


        if (params.f && params.f != 'html') {

             response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=baseVeiculos-${new Date().format('yyMMddHHmmss')}.${params.extension}")

            def veiculoReport = Veiculo.executeQuery(sb.toString(),
                    [])

            veiculoReport = veiculoReport.collect {
                [
                        "placa": it[0],
                        "marca": it[1],
                        "modelo": it[2],
                        "rh": it[3],
                        "unidade": it[4],
                        "ano_fabricacao": it[5],
                        "hodometro": it[6],
                ]
            }

            def fields = ["placa", "marca", "modelo", "rh", "unidade", "ano_fabricacao", "hodometro"]

            def labels = [
                    "placa"   : "Placa",
                    "marcar"  : "Marca",
                    "modelo"  : "Modelo",
                    "rh"      : "Empresa",
                    "unidade" : "Unidade",
                    "ano_fabricacao" : "Ano Fabricação",
                    "hodometro" : "Hodometro(Km)"
            ]
            exportService.export(params.f, response.outputStream, veiculoReport, fields, labels, [:], [:])

            return
        }

        def baseVeiculosList = Veiculo.executeQuery(sb.toString(),[],[max: params.max ? params.max as int : 10, offset: params.offset ? params.offset as int : 0])

        return baseVeiculosList

            def baseVeiculosCount = Veiculo.executeQuery(sb.toString(),[])


            [baseVeiculosList: baseVeiculosList, baseVeiculosCount: baseVeiculosCount, params: params]

         //  [baseVeiculosList: baseVeiculosList.list(params), baseVeiculosCount: baseVeiculosCount.count()]

        } */

    def exportService

    GrailsApplication grailsApplication



    BaseVeiculosService  baseVeiculosService


    def index() {




        if (params.f && params.f != 'html') {



            def list = {
                if (!params.max) params.max = 10

                if (params?.format && params.format != "html") {
                    response.contentType = grailsApplication.config.grails.mime.types[params.format]
                    response.setHeader("Content-disposition", "attachment; filename=baseVeiculos.${params.extension}")


                    List fields = ["matricula", "nome", "cpf", "rh", "unidade"]
                    Map labels = ["Matricula": "matricula", "Nome": "nome", "Nome": "nome", "CPF": "nome", "Empresa": "rh", "Unidade": "unidade"]

                    /* Formatter closure in previous releases
                def upperCase = { value ->
                    return value.toUpperCase()
                }


                // Formatter closure
                def upperCase = { domain, value ->
                    return value.toUpperCase()
                }*/

                    //  Map formatters = [author: upperCase]
                    Map parameters = [title: "Base de Veiculos", "column.widths": [0.2, 0.3, 0.5]]

                    exportService.export(params.format, response.outputStream, baseVeiculosService(params), fields, labels, formatters, parameters)


                }




            }


        }

        [baseVeiculosList: baseVeiculosService.list(params), baseVeiculosCount: baseVeiculosService.count()]
    }



    }
