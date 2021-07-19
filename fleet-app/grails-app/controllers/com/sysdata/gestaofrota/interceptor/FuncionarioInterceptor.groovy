package com.sysdata.gestaofrota.interceptor

import com.sysdata.gestaofrota.Funcionario
import com.sysdata.gestaofrota.Rh


class FuncionarioInterceptor {

    def springSecurityService

    FuncionarioInterceptor() {
        match(controller: 'funcionario', action: '*')
    }

    boolean before() {
        def user = springSecurityService.currentUser
        if (user.owner.instanceOf(Rh))
            Funcionario.enableHibernateFilter('funcionariosPorUnidade').setParameter('rh_id', user.owner.id)
        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
