package com.sysdata.gestaofrota.proc.cartao.dsl


class CartaoBuilder {

    static String build(Closure clo) {
        CartaoBuilder builder = new CartaoBuilder()
        clo.resolveStrategy = Closure.DELEGATE_FIRST
        clo.delegate = builder
        clo.call()
    }

    def bin() {
        
    }

    def tipo() {

    }

    def sequencia() {

    }

    def dv() {

    }


}