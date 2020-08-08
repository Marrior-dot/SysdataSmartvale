package com.sysdata.gestaofrota.interceptor

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
        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
