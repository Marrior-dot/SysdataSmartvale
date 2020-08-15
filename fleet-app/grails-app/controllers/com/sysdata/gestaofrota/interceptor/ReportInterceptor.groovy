package com.sysdata.gestaofrota.interceptor

import com.sysdata.gestaofrota.PostoCombustivel
import com.sysdata.gestaofrota.Transacao


class ReportInterceptor {

    def springSecurityService

    ReportInterceptor() {
        match(controller: 'projecaoReembolso', action: '*')
    }


    boolean before() {

        def user = springSecurityService.currentUser

        if (user?.owner?.instanceOf(PostoCombustivel)) {
            Transacao.enableHibernateFilter('transacaoPorPosto').setParameter('emp_id', user.owner.id)
        }

        return true

    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
