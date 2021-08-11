package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Lancamento
import com.sysdata.gestaofrota.Transacao
import grails.gorm.transactions.Transactional

@Transactional
class ProjecaoReembolsoService {

    def list(params, paginate = true) {
        def pars = [:]
        def sb = new StringBuilder()
        sb.append("""
            select
                t.estabelecimento.empresa.nome as razao,
                t.estabelecimento.empresa.nomeFantasia,
                t.estabelecimento.empresa.cnpj,
                l.dataEfetivacao,
                sum(t.valor) as valor_bruto,
                sum(l.valor) as valor_liquido,
                t.taxaAdm,

                t.estabelecimento.empresa.dadoBancario.banco.nome as banco,
                t.estabelecimento.empresa.dadoBancario.agencia,
                t.estabelecimento.empresa.dadoBancario.conta,
                t.estabelecimento.empresa.dadoBancario.nomeTitular,
                t.estabelecimento.empresa.dadoBancario.documentoTitular
            from
                Transacao t,
                Lancamento l
            where
                l.transacao = t and
                l.tipo = 'REEMBOLSO' and
                l.status = 'A_FATURAR'
        """)

        if (params.cnpj) {
            pars.cnpj = params.cnpj
            sb.append(""" and t.estabelecimento.empresa.cnpj = :cnpj """)
        }

        if (params.dataInicio && params.dataFim) {

            pars.dataInicio = params.date('dataInicio', 'dd/MM/yyyy')
            pars.dataFim = params.date('dataFim', 'dd/MM/yyyy')

            sb.append("""
                and l.dataEfetivacao >= :dataInicio and l.dataEfetivacao <= :dataFim """)

        }

        sb.append("""
            group by
                t.estabelecimento.empresa.nome,
                t.estabelecimento.empresa.nomeFantasia,
                t.estabelecimento.empresa.cnpj,
                l.dataEfetivacao,
                t.taxaAdm,
                t.estabelecimento.empresa.dadoBancario.banco.nome,
                t.estabelecimento.empresa.dadoBancario.agencia,
                t.estabelecimento.empresa.dadoBancario.conta,
                t.estabelecimento.empresa.dadoBancario.nomeTitular,
                t.estabelecimento.empresa.dadoBancario.documentoTitular
            order by
                l.dataEfetivacao,
                t.estabelecimento.empresa.nome
        """)

        if (paginate)
            pars += [max: params.max, offset: params.offset]

        return Lancamento.executeQuery(sb.toString(), pars)
    }

    def count(params) {
        def pars = [:]
        def sb = new StringBuilder()
            sb.append("""
                select
                    count(*)
                from
                    Transacao t,
                    Lancamento l

                where
                    l.transacao = t and
                    l.tipo = 'REEMBOLSO' and
                    l.status = 'A_FATURAR'
""")

        if (params.cnpj) {
            pars.cnpj = params.cnpj
            sb.append(""" and t.estabelecimento.empresa.cnpj = :cnpj """)
        }

        if (params.dataInicio && params.dataFim) {
            pars.dataInicio = params.date('dataInicio', 'dd/MM/yyyy')
            pars.dataFim = params.date('dataFim', 'dd/MM/yyyy')

            sb.append("""
                and l.dataEfetivacao >= :dataInicio and l.dataEfetivacao <= :dataFim """)
        }
        sb.append("""
            group by
                t.estabelecimento.empresa.nome,
                t.estabelecimento.empresa.nomeFantasia,
                t.estabelecimento.empresa.cnpj,
                l.dataEfetivacao,
                t.taxaAdm,
                t.estabelecimento.empresa.dadoBancario.banco.nome,
                t.estabelecimento.empresa.dadoBancario.agencia,
                t.estabelecimento.empresa.dadoBancario.conta,
                t.estabelecimento.empresa.dadoBancario.nomeTitular,
                t.estabelecimento.empresa.dadoBancario.documentoTitular
            order by
                l.dataEfetivacao,
                t.estabelecimento.empresa.nome
""")

        def rowsCount = Lancamento.executeQuery(sb.toString(), pars)
        return rowsCount ? rowsCount[0] : 0

    }
}
