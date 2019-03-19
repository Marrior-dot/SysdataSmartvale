package com.sysdata.gestaofrota


class VeiculoService {

    Veiculo alteraHodometro(Veiculo veiculo, long valor, User usuario) {
        if(valor){
            def valorAnterior = veiculo.hodometro
            veiculo.hodometro = valor
            veiculo.save(flush: true)
            try{
                HistoricoHodometro historicoHodometro = new HistoricoHodometro(veiculo:veiculo,hodometroAntigo: valorAnterior, hodometroNovo: veiculo.hodometro, user: usuario)
                historicoHodometro.save()
            }
            catch (Exception e){
                println e
            }

        }
        veiculo
    }
}
