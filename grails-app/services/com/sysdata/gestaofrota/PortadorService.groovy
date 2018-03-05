package com.sysdata.gestaofrota

class PortadorService {

    PortadorFuncionario save(Funcionario funcionario) {
        PortadorFuncionario portadorFuncionario = funcionario.portador
        if (portadorFuncionario.tipoLimite == null) portadorFuncionario.tipoLimite = TipoLimite.MENSAL
        if (portadorFuncionario.valorLimite == null) portadorFuncionario.valorLimite = 0D
        if (portadorFuncionario.unidade == null) portadorFuncionario.unidade = funcionario.unidade

        portadorFuncionario.funcionario = funcionario
        funcionario.portador = portadorFuncionario
        portadorFuncionario.unidade = funcionario.unidade
        if (!portadorFuncionario.save()) throw new RuntimeException(portadorFuncionario.showErrors());
        funcionario.unidade.addToPortadores(portadorFuncionario)
        funcionario.unidade.save()

        portadorFuncionario
    }

    PortadorMaquina save(MaquinaMotorizada maquina,params) {

        if (!maquina.save()) throw new RuntimeException(maquina.showErrors())

        PortadorMaquina portadorMaquina=new PortadorMaquina()
        portadorMaquina.unidade=maquina.unidade
        portadorMaquina.maquina=maquina
        portadorMaquina.limiteTotal=Util.convertToCurrency(params.portador.limiteTotal)
        portadorMaquina.saldoTotal=portadorMaquina.limiteTotal


        if(params.portador.limiteDiario){
            portadorMaquina.limiteDiario=Util.convertToCurrency(params.portador.limiteDiario)
            portadorMaquina.saldoDiario=portadorMaquina.limiteDiario
        }
        if(params.portador.limiteMensal){
            portadorMaquina.limiteMensal=Util.convertToCurrency(params.portador.limiteMensal)
            portadorMaquina.saldoMensal=portadorMaquina.limiteMensal
        }

        if (!portadorMaquina.save()) throw new RuntimeException(portadorMaquina.showErrors())
        maquina.unidade.addToPortadores(portadorMaquina)
        maquina.unidade.save()

        portadorMaquina
    }
}
