package com.sysdata.gestaofrota.proc.faturamento.boleto

import grails.util.Holders


class GeradorBoletoFactory {

    static GeradorBoleto getGerador() {
        GeradorBoleto geradorBoleto

        def nomeGerador = Holders.grailsApplication.config.projeto.faturamento.portador.boleto.gerador
        if (nomeGerador) {
            geradorBoleto = Holders.grailsApplication.mainContext.getBean(nomeGerador)
            return geradorBoleto
        }
        else
            throw new RuntimeException("Gerador Boleto não definido na configuração")

    }

}