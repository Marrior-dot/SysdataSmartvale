package com.sysdata.gestaofrota.cartao

import com.sysdata.gestaofrota.Funcionario
import com.sysdata.gestaofrota.ParametroSistema
import grails.util.Holders

class NewGeradorCartaoService extends GeradorCartaoService {

    @Override
    String gerarNumero(Funcionario funcionario) {
        def binPar=ParametroSistema.findByNome(ParametroSistema.BIN)
        def bin=binPar.valor
        def tipoProg=Holders.grailsApplication.config.project.tipoPrograma
        def parceiro=Holders.grailsApplication.config.project.parceiro
        def cdRh=funcionario.unidade.rh.id
        def qtde=funcionario.unidade.rh.qtdeContas
        def prov=sprintf("%6d%1d%1d%03d%07d",bin.toInteger(),tipoProg,parceiro,cdRh,++qtde)
        def check=calcularDV(prov)
        funcionario.unidade.rh.qtdeContas=qtde
        funcionario.unidade.rh.save(flush:true)

        prov+check

    }
}
