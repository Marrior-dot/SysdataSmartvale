package com.sysdata.gestaofrota.interceptor

import com.sysdata.gestaofrota.*

class ReportInterceptor {

    def springSecurityService

    ReportInterceptor() {
        match(controller: ~/.*Relatorio/, action: '*')
    }


    boolean before() {

        def user = springSecurityService.currentUser

        if (user?.owner?.instanceOf(PostoCombustivel))
            Transacao.enableHibernateFilter('transacaoPorPosto').setParameter('emp_id', user.owner.id)

        else if (user?.owner?.instanceOf(Rh)) {
            Rh.enableHibernateFilter('empresaPorUser').setParameter('owner_id', user.owner.id)
            Transacao.enableHibernateFilter('transacaoPorRH').setParameter('rh_id', user.owner.id)
            Funcionario.enableHibernateFilter('funcionariosPorUnidade').setParameter('rh_id', user.owner.id)
            MaquinaMotorizada.enableHibernateFilter('maquinasPorRh').setParameter('rh_id', user.owner.id)
        }


        return true

    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
