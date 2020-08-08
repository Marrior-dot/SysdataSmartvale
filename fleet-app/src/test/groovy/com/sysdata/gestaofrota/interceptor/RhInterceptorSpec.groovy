package com.sysdata.gestaofrota.interceptor

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class RhInterceptorSpec extends Specification implements InterceptorUnitTest<RhInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test rh interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"rh")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
