package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.PagamentoEstabelecimento
import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.TipoTransacao
import com.sysdata.gestaofrota.Transacao
import grails.gorm.transactions.Transactional

@Transactional
class DemonstrativoDesempenhoService {

    def list(params, paginate = true) {
        def pars = [:]
        def sb = new StringBuilder()
        sb.append("""
        select
            v.placa,
            v.marca.abreviacao,
            v.modelo,
            f.matricula,
            f.nome,
            v.unidade.rh.nome,
            v.unidade.nome,
            coalesce(tl.quilometragem, 0) - coalesce(tf.quilometragem, 0),
            coalesce((select sum(tc.qtd_litros) from Transacao tc where tc.maquina = v and tc.tipo = :tipo and tc.statusControle in :status), 0),

        (coalesce(tl.quilometragem, 0) - coalesce(tf.quilometragem, 0))
        /
    (coalesce((select sum(tc.qtd_litros) from Transacao tc where tc.maquina = v and tc.tipo = :tipo and tc.statusControle in :status), 0))

        from
            Veiculo v,
            Funcionario f,
            Transacao tf,
            Transacao tl

        where
            tf.maquina = v and
            tf.participante = f and
            tl.maquina = v and
            tl.participante = f and

            tf.id = (select min(id) from Transacao t1 where t1.maquina = v and t1.tipo = :tipo and t1.statusControle in :status) and
            tl.id = (select max(id) from Transacao t2 where t2.maquina = v and t2.tipo = :tipo and t2.statusControle in :status) and

            coalesce(tl.quilometragem, 0) - coalesce(tf.quilometragem, 0) > 0

        """)

        if (params.placa)
            sb.append(" and v.placa = '${params.placa}'")

        else if (params.unidade) {
            sb.append(" and v.unidade.id = ${params.unidade.toLong()}")
        }

        if (paginate)
            pars += [max: params.max, offset: params.offset]

        return Transacao.executeQuery(sb.toString(), [
                tipo:   TipoTransacao.COMBUSTIVEL,
                status: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]
        ], pars)
    }

    def count(params) {
        def sb = new StringBuilder()
        sb.append("""
        select
            count(v.id)
        from
            Veiculo v,
            Funcionario f,
            Transacao tf,
            Transacao tl

        where
            tf.maquina = v and
            tf.participante = f and
            tl.maquina = v and
            tl.participante = f and

            tf.id = (select min(id) from Transacao t1 where t1.maquina = v and t1.tipo = :tipo and t1.statusControle in :status) and
            tl.id = (select max(id) from Transacao t2 where t2.maquina = v and t2.tipo = :tipo and t2.statusControle in :status) and

            coalesce(tl.quilometragem, 0) - coalesce(tf.quilometragem, 0) > 0

        """)

        if (params.placa)
            sb.append(" and v.placa = '${params.placa}'")

        else if (params.unidade) {
            sb.append(" and v.unidade.id = ${params.unidade.toLong()}")
        }

        def rowsCount = Transacao.executeQuery(sb.toString(), [
                tipo:   TipoTransacao.COMBUSTIVEL,
                status: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]
        ])
        return rowsCount ? rowsCount[0] : 0
    }

}
