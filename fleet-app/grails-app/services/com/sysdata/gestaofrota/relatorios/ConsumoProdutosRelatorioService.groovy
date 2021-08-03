package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.Transacao
import grails.gorm.transactions.Transactional

@Transactional
class ConsumoProdutosRelatorioService {

    def list(params, paginate = true) {
        def pars = [:]
        def sb = new StringBuilder()
        sb.append("""
        select
            v.identificacaoCompacta,
            v.unidade.rh.nome,
            v.unidade.nome,
            p.nome,
            sum(t.qtd_litros)
        
        from
            Transacao t,
            Veiculo v,
            TransacaoProduto tp,
            Produto p
        
        where
            v = t.maquina and
            tp.transacao = t and
            tp.produto = p and
            t.statusControle in :status
        """)

        /*if (params.placa)
            sb.append(" and v.placa = ${params.placa}")

        else if (params.unidade) {
            sb.append(" and v.unidade.id = ${params.unidade as long}")
        }*/

       /* if (params.dataInicio && params.dataFim) {
            sb.append(""" and t.dateCreated >= ${params.date('dataInicio', 'dd/MM/yyyy')} and
                t.dateCreated <= ${params.date('dataFim', 'dd/MM/yyyy') + 1} """)
        }*/
        sb.append("""
group by
        v.placa,
        v.marca.abreviacao,
        v.modelo,
        v.unidade.rh.nome,
        v.unidade.nome,
        p.nome
having
    sum(t.qtd_litros) > 0


order by
    v.placa
""")
        def consumoProdutoList
        consumoProdutoList = Transacao.executeQuery(sb.toString(), [
                status: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA],
                max: params.max ? params.max as int : 10, offset: params.offset ? params.offset as int : 0
        ], pars)

        consumoProdutoList = consumoProdutoList.collect {
            [
                    "placa": it[0],
                    "marca": it[1] / it[2],
                    "rh": it[3],
                    "unidade": it[4],
                    "produto": it[5],
                    "consudmo": it[6],
                    //"quilometragem": it[7],
            ]
        }
        return consumoProdutoList
        /*return Transacao.executeQuery(sb.toString(), [
                status: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA],
                max: params.max ? params.max as int : 10, offset: params.offset ? params.offset as int : 0
        ], pars)*/
    }

    def count(params) {
        def sb = new StringBuilder()
        sb.append("""
        select
            count(v.id)        
        from
            Transacao t,
            Veiculo v,
            TransacaoProduto tp,
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

having
    sum(t.qtd_litros) > 0


order by
    v.placa
""")
        def rowsCount = Transacao.executeQuery(sb.toString(),
                [status: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]].size()
        )
        return rowsCount ? rowsCount[0] : 0
    }

}
