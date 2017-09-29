package com.sysdata.gestaofrota

class PortadorService {

    PortadorFuncionario save(Funcionario funcionarioInstance) {
        PortadorFuncionario portadorFuncionario = new PortadorFuncionario()
        portadorFuncionario.funcionario = funcionarioInstance
        funcionarioInstance.portador = portadorFuncionario
        portadorFuncionario.unidade = funcionarioInstance.unidade
        if (!portadorFuncionario.save()) throw new RuntimeException("Erro de regra de negocio.");
        funcionarioInstance.unidade.addToPortadores(portadorFuncionario)
        funcionarioInstance.unidade.save()

        portadorFuncionario
    }
}
