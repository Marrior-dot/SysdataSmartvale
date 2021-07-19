package com.sysdata.gestaofrota.interceptor

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class TransacaoInterceptorSpec extends Specification implements InterceptorUnitTest<TransacaoInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test transacao interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"transacao")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
