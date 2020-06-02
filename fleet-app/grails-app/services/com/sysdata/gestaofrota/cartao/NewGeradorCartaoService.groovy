package com.sysdata.gestaofrota.cartao

import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.PortadorFuncionario
import grails.util.Holders

class NewGeradorCartaoService extends GeradorCartaoService {

    @Override
    String gerarNumero(PortadorFuncionario portador) {
        def binPar = Administradora.list().first()
        def bin = binPar.bin
        def tipoProg = Holders.grailsApplication.config.project.tipoPrograma
        def parceiro = Holders.grailsApplication.config.project.parceiro
        def cdRh = portador.funcionario.unidade.rh.id
        def qtde = portador.funcionario.unidade.rh.qtdeContas
        def prov = sprintf("%6d%1d%1d%03d%07d", bin.toInteger(), tipoProg, parceiro, cdRh, ++qtde)
        def check = calcularDV(prov)
        portador.funcionario.unidade.rh.qtdeContas = qtde
        portador.funcionario.unidade.rh.save(flush: true)

        prov + check
    }
}
