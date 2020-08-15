package com.sysdata.gestaofrota.interceptor

import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Veiculo

class VeiculoInterceptor {

    def springSecurityService

    VeiculoInterceptor() {
        match(controller: 'veiculo', action: '*')
    }

    boolean before() {
        def user = springSecurityService.currentUser
        if (user.owner.instanceOf(Rh))
            Veiculo.enableHibernateFilter('veiculosPorRh').setParameter('rh_id', user.owner.id)
        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
