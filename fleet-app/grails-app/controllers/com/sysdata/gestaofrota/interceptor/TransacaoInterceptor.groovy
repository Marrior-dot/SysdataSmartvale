package com.sysdata.gestaofrota.interceptor

import com.sysdata.gestaofrota.PostoCombustivel
import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Transacao


class TransacaoInterceptor {

    def springSecurityService

    TransacaoInterceptor() {
        match(controller: 'transacao', action: '*')
    }

    boolean before() {
        def user = springSecurityService.currentUser
        if (user?.owner?.instanceOf(Rh))
            Transacao.enableHibernateFilter('transacaoPorRH').setParameter('rh_id', user.owner.id)
        else if (user?.owner?.instanceOf(PostoCombustivel))
            Transacao.enableHibernateFilter('transacaoPorPosto').setParameter('emp_id', user.owner.id)

        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
