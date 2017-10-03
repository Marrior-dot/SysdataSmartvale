package com.sysdata.gestaofrota

class PortadorService {

    PortadorFuncionario save(Funcionario funcionario) {
        PortadorFuncionario portadorFuncionario = new PortadorFuncionario()
        portadorFuncionario.funcionario = funcionario
        funcionario.portador = portadorFuncionario
        portadorFuncionario.unidade = funcionario.unidade
        if (!portadorFuncionario.save()) throw new RuntimeException("Erro de regra de negocio.");
        funcionario.unidade.addToPortadores(portadorFuncionario)
        funcionario.unidade.save()

        portadorFuncionario
    }

    PortadorMaquina save(MaquinaMotorizada maquina) {
        maquina.save()
        PortadorMaquina portadorMaquina = new PortadorMaquina()
        portadorMaquina.unidade = maquina.unidade
        portadorMaquina.maquina = maquina
        maquina.portador = portadorMaquina
        if (!portadorMaquina.save()) throw new RuntimeException(portadorMaquina.errors.allErrors.join(', '))
        maquina.unidade.addToPortadores(portadorMaquina)
        maquina.unidade.save()

        portadorMaquina
    }
}
