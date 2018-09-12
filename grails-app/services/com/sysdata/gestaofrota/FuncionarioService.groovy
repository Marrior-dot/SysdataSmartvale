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


    def sugestoes(String nome){
        nome = nome.toUpperCase()
        String [] partes = nome.split(" ")
        def sugestoes = new ArrayList<>()
        int cont = 0

        for (int j = 1; j < partes.size(); j++) {
            String sugestao = ""
            for (int i = 0; i < partes.size(); i++) {
                if(i <= cont ){
                    sugestao = sugestao + (i==0 ? "" : " ") + partes[i]
                }else{
                    sugestao = sugestao + " " + partes[i].substring(0, 1)
                }
            }

            if (sugestao.length() <= 24){
                sugestoes.add(sugestao)
            }

            cont++
        }

        return sugestoes
    }
}