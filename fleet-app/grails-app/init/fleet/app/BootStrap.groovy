package fleet.app

import com.sysdata.gestaofrota.FixturesService
import com.sysdata.gestaofrota.camel.FleetRoutesService
import com.sysdata.xfiles.FileProcessor
import grails.core.GrailsApplication

class BootStrap {

    GrailsApplication grailsApplication

    FleetRoutesService fleetRoutesService
    FixturesService fixturesService

    def init = { servletContext ->

        FileProcessor.init()

        /* Adição do método ** round ** a classe BigDecimal: arredondamento para baixo. P.ex: 1.5 -> 1.0 (padrão -> halfUp = false) */
        BigDecimal.metaClass.round = { precision, halfUp = true ->
            delegate.setScale(precision, halfUp ? BigDecimal.ROUND_HALF_UP : BigDecimal.ROUND_HALF_DOWN)
        }

        /* Adição do método ** incMonth ** a classe Date: adiciona meses a data em questão */
        Date.metaClass.incMonth = { m ->
            use(groovy.time.TimeCategory) {
                return delegate + m.month
            }
        }

        fixturesService.init()
        fleetRoutesService.init()

        grailsApplication.serviceClasses.each { srv ->
            srv.metaClass.clearSession = {
                def sessFact = grailsApplication.mainContext.sessionFactory
                sessFact.currentSession.flush()
                sessFact.currentSession.clear()
            }
        }
    }

    def destroy = {
    }


}