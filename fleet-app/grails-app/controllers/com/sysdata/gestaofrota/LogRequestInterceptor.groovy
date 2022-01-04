package com.sysdata.gestaofrota


class LogRequestInterceptor {

    def springSecurityService

    LogRequestInterceptor() {
        matchAll()
    }

    boolean before() {
        def username = springSecurityService.isLoggedIn() ? springSecurityService.authentication.name : "unknown"
        def sourceIp = request.getRemoteAddr() ?: 'localhost'
        log.info "${username}@${sourceIp} - params: ${params.findAll { it.key ==~ /^(?!.*[Ss]enha)(?!.*[Pp]assword)([a-zA-Z0-9]+)$/ }}"
        true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
