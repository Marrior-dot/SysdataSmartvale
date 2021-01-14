package fleet.app

import com.fourLions.processingControl.BatchProcessing
import com.fourLions.processingControl.DailySchedule
import com.fourLions.processingControl.ExecutionFrequency
import com.fourLions.processingControl.Processing
import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.camel.FleetRoutesService
import com.sysdata.xfiles.FileProcessor
import grails.core.GrailsApplication
import grails.util.Holders

class BootStrap {

    GrailsApplication grailsApplication

    FleetRoutesService fleetRoutesService
    FixturesService fixturesService

    def init = { servletContext ->

        if (grailsApplication.config.projeto.projectId == "banpara" && grailsApplication.config.projeto.reembolso.banpara.api.jksFile) {
            System.setProperty("javax.net.ssl.trustStore", grailsApplication.config.projeto.reembolso.banpara.api.jksFile)
            System.setProperty("javax.net.ssl.trustStorePassword", grailsApplication.config.projeto.reembolso.banpara.api.password)
        }

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
    }

    def destroy = {
    }


}