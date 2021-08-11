package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Unidade
import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.Transacao
import grails.core.GrailsApplication

class ConsumoProdutosRelatorioController {

    def exportService

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
    p.nome,
    sum(t.qtd_litros),

    (
        (select t1.quilometragem from Transacao t1 where t1.id =
            (select max(tu.id) from Transacao tu where tu.maquina = t.maquina and tu.tipo = 'COMBUSTIVEL'
                and tu.statusControle in ('PENDENTE', 'CONFIRMADA')))

        -

        (select t2.quilometragem from Transacao t2 where t2.id =
            (select min(ti.id) from Transacao ti where ti.maquina = t.maquina and ti.tipo = 'COMBUSTIVEL'
                and ti.statusControle = 'CONFIRMADA'))

    ) as kms_percorridos

from
    Transacao as t,
    Veiculo as v,
    TransacaoProduto as tp,
    Produto p

where
    v = t.maquina and
    tp.transacao = t and
    tp.produto = p and
    t.statusControle in :status
""")

        if (params.placa)
            sb.append(" and v.placa = '${params.placa}' ")

        if (params.dataInicio && params.dataFim) {
            pars.dataInicio = params.date('dataInicio', 'dd/MM/yyyy')
            pars.dataFim = params.date('dataFim', 'dd/MM/yyyy')
            sb.append(""" and v.dateCreated >= :dataInicio and v.dateCreated <= :dataFim """)
        }

        if (params.unidade)
            sb.append(" and v.unidade.id = ${params.unidade as long} ")


        sb.append("""
group by
    v.placa,
    v.marca.abreviacao,
    v.modelo,
    v.unidade.rh.nome,
    v.unidade.nome,
    p.nome,
    t.maquina
having
    sum(t.qtd_litros) > 0


order by
    v.placa
""")


        if (params.f && params.f != 'html') {

            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=consumoCombustivel-${new Date().format('yyMMddHHmmss')}.${params.extension}")

            def cabecalhoDemonstrativoRelatorio = []

            def cabecalho = [:]
            cabecalho.placa = "EMISSAO"
            cabecalho.marca = new Date().format('dd/MM/yyyy')
            cabecalhoDemonstrativoRelatorio << cabecalho

            def cabecalho1 = [:]
            if (params.empresa) {
                cabecalho1.placa = "EMPRESA"
                Rh empresaCliente = Rh.get(params.empresa.toLong())
                cabecalho1.marca = empresaCliente.nomeFantasia
                cabecalhoDemonstrativoRelatorio << cabecalho1
            }
            def cabecalho2 = [:]
            if (params.unidade) {
                cabecalho2.placa = "UNIDADE"
                Unidade unidade = Unidade.get(params.unidade.toLong())
                cabecalho2.marca = unidade.nome
                cabecalhoDemonstrativoRelatorio << cabecalho2
            }
            def cabecalho3 = [:]
            if (params.placa) {
                cabecalho3.placa = "PLACA"
                cabecalho3.marca = params.placa
                cabecalhoDemonstrativoRelatorio << cabecalho3
            }
            def cabecalho4 = [:]
            if (params.dataInicio) {
                cabecalho4.placa = "DT. Inicio"
                cabecalho4.marca = params.dataInicio
                cabecalhoDemonstrativoRelatorio << cabecalho4
            }
            def cabecalho5 = [:]
            if (params.dataFim) {
                cabecalho5.placa = "DT. Fim"
                cabecalho5.marca = params.dataFim
                cabecalhoDemonstrativoRelatorio << cabecalho5
            }

            def cabecalho6 = [:]
            cabecalho6.placa = ""
            cabecalhoDemonstrativoRelatorio << cabecalho6

            def cabecalho7 = [:]
            cabecalho7.placa = "PLACA"
            cabecalho7.marca = "MARCA/MODELO"
            cabecalho7.rh = "CLIENTE"
            cabecalho7.unidade = "UNIDADE"
            cabecalho7.produto = "PRODUTO"
            cabecalho7.consumo = "LTS ABASTECIDOS"
            cabecalho7.quilometragem = "DESEMPENHO (km/l)"
            cabecalhoDemonstrativoRelatorio << cabecalho7

            def consumoReport = Transacao.executeQuery(sb.toString(),
                    [status: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]])

            consumoReport = consumoReport.collect {
                [
                        "placa": it[0],
                        "marca": "${it[1]} / ${it[2]}",
                        "rh": it[3],
                        "unidade": it[4],
                        "produto": it[5],
                        "consumo": it[6],
                        "quilometragem": it[7],
                ]
            }
            consumoReport += [
                    "placa"         : "",
                    "marca"         : "",
                    "rh"            : "",
                    "unidade"       : "",
                    "produto"       : "",
                    "consumo"       : "",
                    "quilometragem" : ""

                    ]

            def fields = [
                    "placa",
                    "marca",
                    "rh",
                    "unidade",
                    "produto",
                    "consumo",
                    "quilometragem"
            ]

            def labels = [
                    "placa"         : "Placa",
                    "marca"         : "Marca",
                    "rh"            : "Empresa",
                    "unidade"       : "Unidade",
                    "produto"       : "Produto",
                    "consumo"       : "Consumo(lts)",
                    "quilometragem" : "KM Percorrida"
            ]

            exportService.export(params.f, response.outputStream, cabecalhoDemonstrativoRelatorio+consumoReport, fields, labels, [:], ['header.enabled': false])

            return
        }

        def consumoList = Transacao.executeQuery(sb.toString(),
                [status: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]],
                [max: params.max ? params.max as int : 10, offset: params.offset ? params.offset as int : 0] )

        def consumoCount = Transacao.executeQuery(sb.toString(),
                [status: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]]).size()

        [consumoList: consumoList, consumoCount: consumoCount, params: params]



    }
}
