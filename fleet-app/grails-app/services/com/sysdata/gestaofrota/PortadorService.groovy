package com.sysdata.gestaofrota

class PortadorService {

    PortadorFuncionario save(params, Funcionario funcionario) {

        PortadorFuncionario portadorFuncionario

        if (funcionario.portador) portadorFuncionario = funcionario.portador
        else portadorFuncionario = new PortadorFuncionario()

        if (portadorFuncionario.unidade == null) portadorFuncionario.unidade = funcionario.unidade


        portadorFuncionario.funcionario = funcionario
        funcionario.portador = portadorFuncionario

        if(funcionario.unidade?.rh?.modeloCobranca==TipoCobranca.POS_PAGO){
            portadorFuncionario.limiteTotal = Util.convertToCurrency(params.portador.limiteTotal)
            portadorFuncionario.saldoTotal = portadorFuncionario.limiteTotal
            if (funcionario.portador.limiteDiario != null || funcionario.portador.limiteDiario !='') {
                portadorFuncionario.limiteDiario = Util.convertToCurrency(params.portador.limiteDiario)
                portadorFuncionario.saldoDiario = portadorFuncionario.limiteDiario
            }
            if (funcionario.portador.limiteMensal != null || funcionario.portador.limiteMensal != '') {
                portadorFuncionario.limiteMensal = Util.convertToCurrency(params.portador.limiteMensal)
                portadorFuncionario.saldoMensal = portadorFuncionario.limiteMensal
            }
            /*if (funcionario.portador.limiteCredito != null || funcionario.portador.limiteCredito != '') {
                portadorFuncionario.limiteCredito = Util.convertToCurrency(params.portador.limiteCredito)
                println "params limite credito"
            }*/
        }/**/


        if (!portadorFuncionario.save()) throw new RuntimeException(portadorFuncionario.showErrors());
        funcionario.unidade.addToPortadores(portadorFuncionario)
        funcionario.unidade.save()

        portadorFuncionario
    }

    PortadorMaquina save(MaquinaMotorizada maquina, params) {
        maquina.save()
        PortadorMaquina portadorMaquina = new PortadorMaquina()
        portadorMaquina.unidade = maquina.unidade
        portadorMaquina.maquina = maquina

        if (maquina.unidade.rh.modeloCobranca == TipoCobranca.POS_PAGO) {

            portadorMaquina.limiteTotal = Util.convertToCurrency(params.portador.limiteTotal)
            portadorMaquina.saldoTotal = portadorMaquina.limiteTotal

            if (params.portador.limiteDiario != null || params.portador.limiteDiario != '') {
                portadorMaquina.limiteDiario = Util.convertToCurrency(params.portador.limiteDiario)
                portadorMaquina.saldoDiario = portadorMaquina.limiteDiario
            }
            if (params.portador.limiteMensal != null || params.portador.limiteMensal != '') {
                portadorMaquina.limiteMensal = Util.convertToCurrency(params.portador.limiteMensal)
                portadorMaquina.saldoMensal = portadorMaquina.limiteMensal
            }
        }

        if (!portadorMaquina.save())
            throw new RuntimeException(portadorMaquina.showErrors())
        maquina.unidade.addToPortadores(portadorMaquina)
        maquina.unidade.save()

        portadorMaquina
    }
}
