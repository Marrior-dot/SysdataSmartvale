package com.sysdata.gestaofrota.interceptor

import com.sysdata.gestaofrota.Rh


class RhInterceptor {

    def springSecurityService

    RhInterceptor() {
        match(controller: 'rh', action: '*')
    }

    boolean before() {
        def user = springSecurityService.currentUser
        if (user?.owner?.instanceOf(Rh))
            Rh.enableHibernateFilter('empresaPorUser').setParameter('owner_id', user.owner.id)


        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
