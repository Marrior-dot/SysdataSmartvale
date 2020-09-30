package com.sysdata.gestaofrota.proc.faturamento.boleto

import com.sysdata.gestaofrota.Boleto

trait GeradorBoleto {

    static GeradorBoleto getGerador(grailsConfig) {
        GeradorBoleto geradorBoleto
        def nomeGerador = grailsConfig.config.project.faturamento.portador.boleto.gerador
        if (nomeGerador) {
            geradorBoleto = grailsConfig.mainContext.getBean(nomeGerador)
            return geradorBoleto
        }
        else
            throw new RuntimeException("Gerador Boleto não definido na configuração")

    }

    abstract void gerarBoleto(Boleto boleto)
}