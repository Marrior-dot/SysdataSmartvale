package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.TipoTransacao
import com.sysdata.gestaofrota.Transacao
import grails.gorm.transactions.Transactional

@Transactional
class DemonstrativoDesempenhoService {

    def list(params) {

        def desempenhoList = Transacao.executeQuery("""
        select
            v.placa,
            v.marca.abreviacao,
            v.modelo,
            f.matricula,
            f.nome,
            v.unidade.rh.nome,
            v.unidade.nome,
            coalesce(tl.quilometragem, 0) - coalesce(tf.quilometragem, 0),

        (coalesce(tl.quilometragem, 0) - coalesce(tf.quilometragem, 0))
        /
    (coalesce((select sum(tc.qtd_litros) from Transacao tc where tc.maquina = v and tc.tipo = :tipo and tc.statusControle in :status), 0)
    - tl.qtd_litros)

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

        """, [
                tipo:   TipoTransacao.COMBUSTIVEL,
                status: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]
        ], [max: params.max ? params.max as int: 10, offset: params.offset ? params.offset as int : 0])

        return desempenhoList
    }

    def count() {
        def desempenhoCount = Transacao.executeQuery("""
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

        """, [
                tipo:   TipoTransacao.COMBUSTIVEL,
                status: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]
        ])

        return desempenhoCount
    }

}
