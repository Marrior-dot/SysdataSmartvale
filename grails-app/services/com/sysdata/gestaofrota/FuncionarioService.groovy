package com.sysdata.gestaofrota

class FuncionarioService {
    def participanteService
    def cartaoService
    def portadorService

    Funcionario save(Funcionario funcionarioInstance, boolean gerarCartao = false) {
        if (!funcionarioInstance.unidade) throw new RuntimeException("Funcionario não possui unidade.")

        if (funcionarioInstance.unidade?.rh?.vinculoCartao == TipoVinculoCartao.FUNCIONARIO
                && funcionarioInstance.portador == null) {
            if(!funcionarioInstance.save()) throw new RuntimeException(funcionarioInstance.errors.toString())
            PortadorFuncionario portadorFuncionario = portadorService.save(funcionarioInstance)
            if (gerarCartao) cartaoService.gerar(portadorFuncionario)
        }

        participanteService.saveCidade(funcionarioInstance.endereco)
        if (!funcionarioInstance.save(flush: true)) throw new RuntimeException("Erro de regra de negocio.")

        funcionarioInstance
    }
}