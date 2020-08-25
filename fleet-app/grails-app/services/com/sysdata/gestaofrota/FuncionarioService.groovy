package com.sysdata.gestaofrota

class FuncionarioService {
    def cartaoService

    def save(Funcionario funcionarioInstance, boolean gerarCartao = false) {

        def ret = [success: true]

        if (funcionarioInstance.unidade?.rh?.vinculoCartao == TipoVinculoCartao.FUNCIONARIO) {
            if (! funcionarioInstance.save(flush: true)) {
                ret.success = false
                return ret
            }

            PortadorFuncionario portadorFuncionario = funcionarioInstance.portador
            portadorFuncionario.save(flush: true)

            if (gerarCartao){
                if (portadorFuncionario.funcionario.unidade.rh.cartaoComChip)
                    cartaoService.gerar(portadorFuncionario)
                else
                    cartaoService.gerar(portadorFuncionario, false)
            }
        }
        //participanteService.saveCidade(funcionarioInstance.endereco)
        if (! funcionarioInstance.save(flush: true))
            ret.success = false

        return ret
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

    def delete(Funcionario funcionario) {
        if (funcionario.portador && funcionario.portador.cartaoAtivo) {
            Cartao cartao = funcionario.portador.cartaoAtivo
            cartao.status = StatusCartao.CANCELADO
            cartao.save()
            log.info "CRT #$cartao.id cancelado"

            funcionario.status = Status.INATIVO
            funcionario.save(flush: true)
            log.info "FUNC #$funcionario.id inativado"

        } else if (funcionario.portador) {
            Portador portador = funcionario.portador

            def funcId = funcionario.id

            def prtId = portador.id

            Cartao cartao = portador.cartaoAtual
            if (cartao) {
                def crtId = cartao.id
                cartao.delete()
                log.info "CRT #$crtId del"
            }

            portador.delete()
            log.info "PRT #$prtId del"
            funcionario.delete(flush: true)
            log.info "FUNC #$funcId del"


        } else if (funcionario.veiculos) {
            funcionario.status = Status.INATIVO
            funcionario.save(flush: true)
            log.info "FUNC #$funcionario.id inativado"
        }
    }
}