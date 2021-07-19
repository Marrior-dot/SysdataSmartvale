package com.sysdata.gestaofrota

class TransferenciaSaldo {

    User usuario
    String cartaoTransferir
    String cartaoReceber
    Date dataHora
    Double valor


    static constraints = {
        cartaoTransferir nullable: false
        cartaoReceber nullable: false
        dataHora nullable: false
        valor nullable: false
    }
}
