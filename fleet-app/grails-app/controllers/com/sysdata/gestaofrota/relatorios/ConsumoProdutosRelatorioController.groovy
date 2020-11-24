package com.sysdata.gestaofrota.relatorios

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
    sum(t.quilometragem)


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
    p.nome
having
    sum(t.qtd_litros) > 0 and
    sum(t.quilometragem) > 0


order by
    v.placa
""")


        if (params.f && params.f != 'html') {

            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=consumoCombustivel-${new Date().format('yyMMddHHmmss')}.${params.extension}")

            def consumoReport = Transacao.executeQuery(sb.toString(),
                                                        [status: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]])

            consumoReport = consumoReport.collect {
                                [
                                    "placa": it[0],
                                    "marca": it[1],
                                    "modelo": it[2],
                                    "rh": it[3],
                                    "unidade": it[4],
                                    "produto": it[5],
                                    "consumo": it[6],
                                    "quilometragem": it[7],
                                ]
                            }

            def fields = ["placa", "marca", "modelo", "rh", "unidade", "produto", "consumo", "Km Percorida "]

            def labels = [
                            "placa"   : "Placa",
                            "marcar"  : "Marca",
                            "modelo"  : "Modelo",
                            "rh"      : "Empresa",
                            "unidade" : "Unidade",
                            "produto" : "Produto",
                            "consumo" : "Consumo(lts)",
                            "quilometragem" : "KM Percorrida"
                        ]

            exportService.export(params.f, response.outputStream, consumoReport, fields, labels, [:], [:])

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
