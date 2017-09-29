package com.sysdata.gestaofrota.cartao

import com.sysdata.gestaofrota.Funcionario
import com.sysdata.gestaofrota.ParametroSistema
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.PortadorFuncionario

class OldGeradorCartaoService extends GeradorCartaoService {
    @Override
    String gerarNumero(PortadorFuncionario portador) {
        def binPar = ParametroSistema.findByNome(ParametroSistema.BIN)
        def bin = binPar.valor
        def qtde = portador.funcionario.cartoes ? funcionario.cartoes.size() : 0
        def cdRh = portador.funcionario.unidade.rh.id
        def idFunc = portador.funcionario.id
        def prov = sprintf("%6d%1d%03d%06d%02d", bin.toInteger(), 4, cdRh.toInteger(), idFunc, ++qtde)
        def check = calcularDV(prov)
        prov + check
    }
}
