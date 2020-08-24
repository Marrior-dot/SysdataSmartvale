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

                                else if (obj.class.findByCnpjAndStatus(val, Status.ATIVO))
                                    return "cnpj.existente"
                            }

        nomeFantasia(blank: false)
        inscricaoEstadual(nullable: true)
        inscricaoMunicipal(nullable: true)
    }
}
