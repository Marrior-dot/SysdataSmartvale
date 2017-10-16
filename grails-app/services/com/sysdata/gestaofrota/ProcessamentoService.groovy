package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.processamento.administradoras.AdministradoraCartao
import com.sysdata.gestaofrota.processamento.administradoras.MaxCard
import com.sysdata.gestaofrota.processamento.administradoras.Sysdata
import com.sysdata.gestaofrota.processamento.embossadoras.Embossadora
import com.sysdata.gestaofrota.processamento.embossadoras.IntelCav
import com.sysdata.gestaofrota.processamento.embossadoras.PaySmart
import grails.util.Holders

class ProcessamentoService {

    AdministradoraCartao getAdministradora() {
        //carregando do arquivo config
        TipoAdministradoraCartao tipo =
                Holders.grailsApplication.config.project.tipoAdministradora

        if (tipo == TipoAdministradoraCartao.SYSDATA) return new Sysdata()
        else if (tipo == TipoAdministradoraCartao.MAXCARD) return new MaxCard()
        //TODO enviar as outras

        throw new RuntimeException("Administradora não configurada no arquivo Config.groovy ou não implementada")
    }

    Embossadora getEmbossadora(){
        //carregando do arquivo config
        TipoEmbossadora tipo = Holders.grailsApplication.config.project.tipoEmbossadora

        if(tipo == TipoEmbossadora.PAYSMART) return new PaySmart(null)
        else if(tipo == TipoEmbossadora.INTELCAV) return new IntelCav(null)

        throw new RuntimeException("Embossadora não configurada no arquivo Config.groovy ou não implementada")
    }
}
