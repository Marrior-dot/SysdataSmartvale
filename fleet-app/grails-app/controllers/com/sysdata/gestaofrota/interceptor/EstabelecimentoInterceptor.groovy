package com.sysdata.gestaofrota.interceptor

import com.sysdata.gestaofrota.Lancamento
import com.sysdata.gestaofrota.PagamentoEstabelecimento
import com.sysdata.gestaofrota.PostoCombustivel
import com.sysdata.gestaofrota.Transacao

class EstabelecimentoInterceptor {

    def springSecurityService

    EstabelecimentoInterceptor() {
        match(controller: ~/reembolsoFaturadoRelatorio|projecaoReembolsoRelatorio|postoCombustivel/ )
    }

    boolean before() {
        def user = springSecurityService.currentUser
        if (user?.owner?.instanceOf(PostoCombustivel)) {
            PagamentoEstabelecimento.enableHibernateFilter('reembolsoPorEstabelecimento')
                    .setParameter('est_id', user.owner.id)
            Lancamento.enableHibernateFilter('lancamentoPorPosto')
                    .setParameter('emp_id', user.owner.id)
            PostoCombustivel.enableHibernateFilter('estabelecimentoPorPosto')
                    .setParameter('emp_id', user.owner.id)
        }
        true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
