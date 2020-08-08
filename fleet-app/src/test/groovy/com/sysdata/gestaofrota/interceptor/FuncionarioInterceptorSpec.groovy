package com.sysdata.gestaofrota.interceptor

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class FuncionarioInterceptorSpec extends Specification implements InterceptorUnitTest<FuncionarioInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test funcionario interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"funcionario")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
