package com.sysdata.gestaofrota.camel

import grails.gorm.transactions.Transactional
import grails.util.Environment

@Transactional
class FleetRoutesService {

    PaysmartEmbossingRoutesService paysmartEmbossingRoutesService
    IntelcavEmbossingRoutesService intelcavEmbossingRoutesService

    def initRoutes() {
        if (Environment.current == Environment.PRODUCTION) {
            paysmartEmbossingRoutesService.init()
            intelcavEmbossingRoutesService.init()
        }
    }


}
