package com.sysdata.gestaofrota

class ReembolsoDias extends Reembolso {

    Integer diasTranscorridos

    static constraints = {

        diasTranscorridos blank: false, validator: { val, obj ->
                                            if (! (val ==~ /\d+/ && val.toInteger() > 0) )
                                                return "reembolsoDias.diasTrascorridos.numeroInvalido"

                                        }
    }
}
