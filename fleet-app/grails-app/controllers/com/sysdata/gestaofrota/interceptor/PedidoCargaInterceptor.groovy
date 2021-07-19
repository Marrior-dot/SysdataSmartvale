package com.sysdata.gestaofrota.interceptor

import com.sysdata.gestaofrota.PedidoCarga
import com.sysdata.gestaofrota.Rh


class PedidoCargaInterceptor {

    def springSecurityService

    PedidoCargaInterceptor() {
        match(controller: 'pedidoCarga', action: '*')
    }

    boolean before() {
        def user = springSecurityService.currentUser
        if (user?.owner?.instanceOf(Rh)) {
            PedidoCarga.enableHibernateFilter('pedidoPorRh').setParameter('rh_id', user.owner.id)
            Rh.enableHibernateFilter('empresaPorUser').setParameter('owner_id', user.owner.id)
        }

        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
