package com.sysdata.gestaofrota


class LogRequestInterceptor {

    def springSecurityService

    LogRequestInterceptor() {
        matchAll()
    }

    boolean before() {
        def username = springSecurityService.isLoggedIn() ? springSecurityService.authentication.name : "unknown"
        def sourceIp = request.getHeader('x-forwarded-for') ?: 'localhost'
        log.info "${username}@${sourceIp} - params: ${params}"
        true

    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
