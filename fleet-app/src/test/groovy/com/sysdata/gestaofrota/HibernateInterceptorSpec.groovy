package com.sysdata.gestaofrota

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class HibernateInterceptorSpec extends Specification implements InterceptorUnitTest<HibernateInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test hibernate interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"hibernate")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
