package com.sysdata.gestaofrota

class Empresa extends Participante {
    String cnpj
    String nomeFantasia
    String inscricaoEstadual
    String inscricaoMunicipal

    static constraints = {
        cnpj blank: false , validator: { val, obj ->

                                if (! Util.validarCnpj(val))
                                    return "empresa.cnpj.invalido"

                                def mesmoCnpj = obj.class.withCriteria {
                                                    eq('cnpj', val)
                                                    'in'('status', [Status.ATIVO, Status.BLOQUEADO])
                                                }

                                if (mesmoCnpj && mesmoCnpj[0].id != obj.id)
                                    return "empresa.cnpj.existente"
                            }

        nomeFantasia(blank: false)
        inscricaoEstadual(nullable: true)
        inscricaoMunicipal(nullable: true)
    }

    static transients = ['documentoSemMascara', 'identificacaoResumida']

    String getDocumentoSemMascara() {
        return Util.cnpjToRaw(this.cnpj)
    }

    String getIdentificacaoResumida() {
        return "(${this.cnpj}) ${this.nomeFantasia}"
    }


}
