package com.sysdata.gestaofrota

class PortadorService {

    PortadorFuncionario save(params,Funcionario funcionario) {

        PortadorFuncionario portadorFuncionario

        if(funcionario.portador) portadorFuncionario = funcionario.portador
        else portadorFuncionario = new PortadorFuncionario()

        if (portadorFuncionario.unidade == null) portadorFuncionario.unidade = funcionario.unidade


        portadorFuncionario.limiteTotal=Util.convertToCurrency(params.portador.limiteTotal)
        portadorFuncionario.saldoTotal=portadorFuncionario.limiteTotal


        if(params.portador.limiteDiario){
            portadorFuncionario.limiteDiario=Util.convertToCurrency(params.portador.limiteDiario)
            portadorFuncionario.saldoDiario=portadorFuncionario.limiteDiario
        }
        if(params.portador.limiteMensal){
            portadorFuncionario.limiteMensal=Util.convertToCurrency(params.portador.limiteMensal)
            portadorFuncionario.saldoMensal=portadorFuncionario.limiteMensal
        }


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
