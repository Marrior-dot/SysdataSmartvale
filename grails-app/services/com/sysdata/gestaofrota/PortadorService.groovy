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

    PortadorMaquina save(Equipamento equipamento) {
        PortadorMaquina portadorMaquina = new PortadorMaquina()
        portadorMaquina.maquina = equipamento
        equipamento.portador = portadorMaquina
        portadorMaquina.unidade = equipamento.unidade
        if (!portadorMaquina.save()) throw new RuntimeException("Erro de regra de negocio.");
        equipamento.unidade.addToPortadores(portadorMaquina)
        equipamento.unidade.save()

        portadorMaquina
    }
}
