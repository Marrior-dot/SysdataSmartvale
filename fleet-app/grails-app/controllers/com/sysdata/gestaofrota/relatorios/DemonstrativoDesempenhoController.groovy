package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.TipoTransacao
import com.sysdata.gestaofrota.Transacao
import com.sysdata.gestaofrota.Veiculo
import org.hibernate.criterion.DetachedCriteria

class DemonstrativoDesempenhoController {

    def index() {

        def desempenhoList = Transacao.executeQuery("""
select
    v.placa,
    v.marca.abreviacao,
    v.modelo,
    v.unidade.rh.nome,
    v.unidade.nome,
    coalesce(tl.quilometragem, 0) - coalesce(tf.quilometragem, 0),
    coalesce((select sum(tc.qtd_litros) from Transacao tc where tc.maquina = v and tc.tipo in :tipo and tc.statusControle in :status), 0),

    (coalesce(tl.quilometragem, 0) - coalesce(tf.quilometragem, 0))
    /
    (coalesce((select sum(tc.qtd_litros) from Transacao tc where tc.maquina = v and tc.tipo in :tipo and tc.statusControle in :status), 0)
    - tl.qtd_litros)
from
    Veiculo v,
    Transacao tf,
    Transacao tl

where
    tf.maquina = v and
    tl.maquina = v and

    tf.id = (select min(id) from Transacao t1 where t1.maquina = v and t1.tipo in :tipo and t1.statusControle in :status) and
    tl.id = (select max(id) from Transacao t2 where t2.maquina = v and t2.tipo in :tipo and t2.statusControle in :status) and

    tf.quilometragem > 0 and tl.quilometragem > 0 and tf.qtd_litros > 0 and tl.qtd_litros > 0

""", [
        tipo:   [TipoTransacao.COMBUSTIVEL, TipoTransacao.SERVICOS],
        status: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]
    ], [max: params.max ? params.max as int: 10, offset: params.offset ? params.offset as int : 0])


        if (params.placa) {

        }

        [desempenhoList: desempenhoList, desempenhoCount: 10]

    }
}
