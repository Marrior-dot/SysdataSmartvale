package com.sysdata.gestaofrota.cartao

import com.sysdata.gestaofrota.Funcionario
import com.sysdata.gestaofrota.ParametroSistema

class OldGeradorCartaoService extends GeradorCartaoService {


    @Override
    String gerarNumero(Funcionario funcionario) {
        def binPar=ParametroSistema.findByNome(ParametroSistema.BIN)
        def bin=binPar.valor
        def qtde=funcionario.cartoes?funcionario.cartoes.size():0
        def cdRh=funcionario.unidade.rh.id
        def idFunc=funcionario.id
        def prov=sprintf("%6d%1d%03d%06d%02d",bin.toInteger(),4,cdRh.toInteger(),idFunc,++qtde)
        def check=calcularDV(prov)
        prov+check

    }
}
