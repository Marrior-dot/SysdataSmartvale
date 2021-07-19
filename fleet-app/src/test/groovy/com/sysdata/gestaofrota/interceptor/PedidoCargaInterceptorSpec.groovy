package com.sysdata.gestaofrota.interceptor

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class PedidoCargaInterceptorSpec extends Specification implements InterceptorUnitTest<PedidoCargaInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test pedidoCarga interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"pedidoCarga")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
