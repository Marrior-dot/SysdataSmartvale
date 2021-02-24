package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.PagamentoEstabelecimento
import grails.gorm.transactions.Transactional

@Transactional
class ReembolsoFaturadoService {

    def list(params, paginate = true) {
        def pars = [:]
        def sb = new StringBuilder()
        sb.append("""
            select
                p.estabelecimento.nome as razao,
                p.estabelecimento.nomeFantasia,
                p.estabelecimento.cnpj,
                p.dataProgramada,
                p.valorBruto,
                p.valor as valorLiquido,
                p.taxaAdm,
                p.valorBruto - p.valor as valorTaxaAdm,
                p.estabelecimento.dadoBancario.banco.nome as banco,
                p.estabelecimento.dadoBancario.agencia,
                p.estabelecimento.dadoBancario.conta,
                p.estabelecimento.dadoBancario.nomeTitular,
                p.estabelecimento.dadoBancario.documentoTitular

            from
                PagamentoEstabelecimento p

            where
                p.status <> 'CANCELADO'""")

        if (params.dataInicio && params.dataFim) {
            pars.dataInicio = params.date('dataInicio', 'dd/MM/yyyy')
            pars.dataFim = params.date('dataFim', 'dd/MM/yyyy')
            sb.append(""" and p.dataProgramada >= :dataInicio and p.dataProgramada <= :dataFim """)
        }

        sb.append("""order by p.dataProgramada desc, p.estabelecimento.nome desc""")

        if (paginate)
            pars += [max: params.max, offset: params.offset]

        return PagamentoEstabelecimento.executeQuery(sb.toString(), pars)
    }

    def count(params) {
        def pars = [:]
        def sb = new StringBuilder()
        sb.append("""
            select
                count(p.id)
            from
                PagamentoEstabelecimento p
            where
                p.status <> 'CANCELADO'""")


        if (params.dataInicio && params.dataFim) {
            pars.dataInicio = params.date('dataInicio', 'dd/MM/yyyy')
            pars.dataFim = params.date('dataFim', 'dd/MM/yyyy')
            sb.append(""" and p.dataProgramada >= :dataInicio and p.dataProgramada <= :dataFim """)
        }
        return PagamentoEstabelecimento.executeQuery(sb.toString(), pars)
    }
}
