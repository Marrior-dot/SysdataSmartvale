package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.Transacao
import grails.gorm.transactions.Transactional

@Transactional
class ConsumoProdutosRelatorioService {

    def list(pars, paginate = true) {
        def sqlParams = [:]

        def sb = new StringBuilder()
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

        if (pars.placa) {
            sqlParams.placa = pars.placa
            sb.append(" and v.placa = :placa")
        }

        if (pars.empresa) {
            sqlParams.empresa = pars.empresa as long
            sb.append(" and v.unidade.rh.id = :empresa")
        }

        if (pars.unidade) {
            sqlParams.unidade = pars.unidade as long
            sb.append(" and v.unidade.id = :unidade")
        }

        if (pars.dataInicio && pars.dataFim) {
            sqlParams.dataInicio = pars.date('dataInicio', 'dd/MM/yyyy')
            sqlParams.dataFim = pars.date('dataFim', 'dd/MM/yyyy') + 1
            sb.append(""" and t.dateCreated >= :dataInicio and t.dateCreated <= :dataFim """)
        }
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
        return Transacao.executeQuery(sb.toString(),
                                        [status: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]] + sqlParams,
                                        pars)
    }


    def count(pars) {
        def sqlParams = [:]

        def sb = new StringBuilder()
        sb.append("""
        select
            count(distinct v.id)
        from
            Transacao t,
            Veiculo v,
            TransacaoProduto tp,
            Produto p
        
        where
            v = t.maquina and
            tp.transacao = t and
            tp.produto = p and
            t.statusControle in :status and
            t.qtd_litros > 0
        """)

        if (pars.placa) {
            sqlParams.placa = pars.placa
            sb.append(" and v.placa = :placa")
        }

        if (pars.empresa) {
            sqlParams.empresa = pars.empresa as long
            sb.append(" and v.unidade.rh.id = :empresa")
        }

        if (pars.unidade) {
            sqlParams.unidade = pars.unidade as long
            sb.append(" and v.unidade.id = :unidade")
        }

        if (pars.dataInicio && pars.dataFim) {
            sqlParams.dataInicio = pars.date('dataInicio', 'dd/MM/yyyy')
            sqlParams.dataFim = pars.date('dataFim', 'dd/MM/yyyy') + 1
            sb.append(""" and t.dateCreated >= :dataInicio and t.dateCreated <= :dataFim """)
        }

        def rowsCount = Transacao.executeQuery(sb.toString(),
                                                [status: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]] + sqlParams)
        return rowsCount ? rowsCount[0] : 0
    }

}
