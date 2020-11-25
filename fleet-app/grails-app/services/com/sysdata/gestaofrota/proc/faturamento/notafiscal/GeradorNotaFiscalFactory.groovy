package com.sysdata.gestaofrota.proc.faturamento.notafiscal

import grails.util.Holders


class GeradorNotaFiscalFactory {

    static GeradorNotaFiscal getGerador() {
        GeradorNotaFiscal geradorNotaFiscal

        def nomeGerador = Holders.grailsApplication.config.projeto.faturamento.portador.notaFiscal.gerador
        if (nomeGerador) {
            geradorNotaFiscal = Holders.grailsApplication.mainContext.getBean(nomeGerador)
            return geradorNotaFiscal
        } else
            throw new RuntimeException("Gerador Nota Fiscal não definido na configuração")
    }
}