package com.sysdata.gestaofrota.cartao

import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.Funcionario
import com.sysdata.gestaofrota.ParametroSistema
import grails.util.Holders

/**
 * Created by luiz on 29/07/16.
 */
class NewGeradorCartao extends GeradorCartao {

    @Override
    String gerarNumero(Funcionario funcionario) {


        Administradora

        def binPar=ParametroSistema.findByNome(ParametroSistema.BIN)
        def bin=binPar.valor
        def tipoProg=Holders.grailsApplication.config.project.tipoPrograma
        def parceiro=Holders.grailsApplication.config.project.parceiro
        def cdRh=funcionario.unidade.rh.id
        def qtde=funcionario.unidade.rh.qtdeContas?funcionario.unidade.rh.qtdeContas++:1
        def prov=sprintf("%6d%1d%1d%03d%07d",bin.toInteger(),tipoProg,parceiro,cdRh,qtde)
        def check=calcularDV(prov)
        prov+check

    }
}
