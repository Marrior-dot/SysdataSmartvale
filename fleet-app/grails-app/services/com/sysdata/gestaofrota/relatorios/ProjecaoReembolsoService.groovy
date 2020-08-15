package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Lancamento
import grails.gorm.transactions.Transactional

@Transactional
class ProjecaoReembolsoService {

    def list(params, paginate = true) {

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
                t.estabelecimento.empresa.dadoBancario.nomeTitular,
                t.estabelecimento.empresa.dadoBancario.documentoTitular,
                t.estabelecimento.empresa.dadoBancario.conta

            from
                Transacao t,
                Lancamento l

            where
                l.transacao = t and
                l.tipo = 'REEMBOLSO' and
                l.status = 'A_EFETIVAR'
        """)


        if (params.dataInicio && params.dataFim)

            sb.append("""
                and l.dataEfetivacao >= ${params.date('dataInicio', 'dd/MM/yyyy')} and
                l.dataEfetivacao <= ${params.date('dataFim', 'dd/MM/yyyy')}""")

        sb.append("""
            group by
                t.estabelecimento.empresa.nome,
                t.estabelecimento.empresa.nomeFantasia,
                t.estabelecimento.empresa.cnpj,
                l.dataEfetivacao,
                t.taxaAdm,
                t.estabelecimento.empresa.dadoBancario.banco.nome,
                t.estabelecimento.empresa.dadoBancario.agencia,
                t.estabelecimento.empresa.dadoBancario.nomeTitular,
                t.estabelecimento.empresa.dadoBancario.documentoTitular,
                t.estabelecimento.empresa.dadoBancario.conta


            order by
                l.dataEfetivacao,
                t.estabelecimento.empresa.nome

        """)

        def pars = [:]
        if (paginate)
            pars = [max: params.max, offset: params.offset]

        return Lancamento.executeQuery(sb.toString(), pars)


    }

    def count(params) {

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
                    l.status = 'A_EFETIVAR'
""")


        if (params.dataInicio && params.dataFim)

            sb.append("""
        and l.dataEfetivacao >= ${params.date('dataInicio', 'dd/MM/yyyy')} and
        l.dataEfetivacao <= ${params.date('dataFim', 'dd/MM/yyyy')}""")


        sb.append("""
            group by
                t.estabelecimento.empresa.nome,
                t.estabelecimento.empresa.nomeFantasia,
                t.estabelecimento.empresa.cnpj,
                l.dataEfetivacao,
                t.taxaAdm,
                t.estabelecimento.empresa.dadoBancario.banco.nome,
                t.estabelecimento.empresa.dadoBancario.agencia,
                t.estabelecimento.empresa.dadoBancario.nomeTitular,
                t.estabelecimento.empresa.dadoBancario.documentoTitular,
                t.estabelecimento.empresa.dadoBancario.conta

            order by
                l.dataEfetivacao,
                t.estabelecimento.empresa.nome

""")

        return Lancamento.executeQuery(sb.toString())

    }
}
