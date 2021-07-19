package com.sysdata.gestaofrota.interceptor

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class EstabelecimentoInterceptorSpec extends Specification implements InterceptorUnitTest<EstabelecimentoInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test estabelecimento interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"estabelecimento")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
