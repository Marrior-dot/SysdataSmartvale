package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class TestesService {

    def list() {

        def hql = """
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
                l.tipo = :tipo and
                l.status = :status

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
"""

        def list = Transacao.executeQuery(hql, [tipo: TipoLancamento.REEMBOLSO, status: StatusLancamento.A_EFETIVAR])

        println "Lista: ${list}"

    }
}
