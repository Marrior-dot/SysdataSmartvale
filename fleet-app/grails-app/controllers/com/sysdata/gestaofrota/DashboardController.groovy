package com.sysdata.gestaofrota

class DashboardController {

    def springSecurityService

    def index() {

        if (! springSecurityService.isLoggedIn())
            redirect(controller: 'login', action: 'auth')
    }

}
