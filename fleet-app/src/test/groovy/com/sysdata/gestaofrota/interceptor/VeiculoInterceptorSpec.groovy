package com.sysdata.gestaofrota.interceptor

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class VeiculoInterceptorSpec extends Specification implements InterceptorUnitTest<VeiculoInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test veiculo interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"veiculo")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
