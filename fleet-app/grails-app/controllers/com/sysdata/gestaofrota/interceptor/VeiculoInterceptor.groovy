package com.sysdata.gestaofrota.interceptor

import com.sysdata.gestaofrota.MaquinaMotorizada
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
            MaquinaMotorizada.enableHibernateFilter('maquinasPorRh').setParameter('rh_id', user.owner.id)
        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
