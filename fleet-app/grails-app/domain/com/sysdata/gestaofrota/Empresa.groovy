package com.sysdata.gestaofrota

class Empresa extends Participante {
    String cnpj
    String nomeFantasia
    String inscricaoEstadual
    String inscricaoMunicipal

    static constraints = {
        cnpj blank: false , validator: { val, obj ->

                                if (! Util.validarCnpj(val))
                                    return "cnpj.invalido"

                                def mesmoCnpj = obj.class.withCriteria {
                                                    eq('cnpj', val)
                                                    'in'('status', [Status.ATIVO, Status.BLOQUEADO])
                                                }

                                if (mesmoCnpj)
                                    return "cnpj.existente"
                            }

        nomeFantasia(blank: false)
        inscricaoEstadual(nullable: true)
        inscricaoMunicipal(nullable: true)
    }
}
