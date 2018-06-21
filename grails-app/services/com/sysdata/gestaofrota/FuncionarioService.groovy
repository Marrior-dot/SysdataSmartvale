package com.sysdata.gestaofrota

class FuncionarioService {
    def participanteService
    def cartaoService
    def portadorService

    Funcionario save(params, Funcionario funcionarioInstance, boolean gerarCartao = false) {
        if (!funcionarioInstance.unidade) throw new RuntimeException("Funcionario não possui unidade.")

        if (funcionarioInstance.unidade?.rh?.vinculoCartao == TipoVinculoCartao.FUNCIONARIO) {
            if (!funcionarioInstance.save()) throw new RuntimeException(funcionarioInstance.showErrors())
            PortadorFuncionario portadorFuncionario = portadorService.save(params, funcionarioInstance)
            if (gerarCartao){
                if(portadorFuncionario.funcionario.unidade.rh.cartaoComChip){
                    cartaoService.gerar(portadorFuncionario)
                }else{
                    cartaoService.gerar(portadorFuncionario,false)
                }

            }

        }

        participanteService.saveCidade(funcionarioInstance.endereco)
        if (!funcionarioInstance.save()) throw new RuntimeException("Erro de regra de negocio.")

        funcionarioInstance
    }
}