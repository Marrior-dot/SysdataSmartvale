package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

class FuncionarioService {
    def cartaoService

    @Transactional
    def save(Funcionario funcionarioInstance, boolean gerarCartao = false) {

        def ret = [success: true]

        if (funcionarioInstance.unidade?.rh?.vinculoCartao == TipoVinculoCartao.FUNCIONARIO) {

            if (! funcionarioInstance.save(flush: true)) {
                ret.success = false
                return ret
            }

            PortadorFuncionario portadorFuncionario = funcionarioInstance.portador
            portadorFuncionario.save(flush: true)

            if (portadorFuncionario.vincularCartao) {
                //Gera cartão somente se por cadastro (insert)
                if (gerarCartao){
                    if (portadorFuncionario.funcionario.unidade.rh.cartaoComChip)
                        cartaoService.gerar(portadorFuncionario)
                    else
                        cartaoService.gerar(portadorFuncionario, false)
                }
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

    @Transactional
    def delete(Funcionario funcionario) {
        def transacaoCount = Transacao.withCriteria(uniqueResult: true) {
                                projections {
                                    rowCount()
                                }
                                eq("participante", funcionario)
                            }
        if (transacaoCount > 0) {
            Cartao cartao = funcionario.portador.cartaoAtivo
            cartao.status = StatusCartao.CANCELADO
            cartao.save()
            log.info "CRT #$cartao.id cancelado"
            funcionario.status = Status.INATIVO
            funcionario.save(flush: true)
            log.info "FUNC #$funcionario.id inativado"
        } else if (funcionario.portador) {
            Portador portador = funcionario.portador
            if (portador.cartoes) {
                portador.cartoes.each { crt ->
                    HistoricoStatusCartao.executeUpdate("delete from HistoricoStatusCartao hsc where hsc.cartao =:crt", [crt: crt])
                    log.info "(-) CRT #${crt.id}"
                    crt.delete()
                }
            }
            def funcId = funcionario.id
            def prtId = portador.id
            portador.delete()
            log.info "(-) PRT #$prtId"
            funcionario.delete(flush: true)
            log.info "(-) FCN #$funcId"
        } else {
            log.info "(-) FCN ${funcionario.id}"
            funcionario.delete(flush: true)
        }
    }


    def list(args) {
        def criteria = {
            if (args.unidId) {
                unidade {
                    idEq(args.unidId.toLong())
                }
            }
            if (args.cpf)
                ilike("cpf", args.cpf + '%')
            if (args.nome)
                ilike("nome", args.nome + '%')
            if (args.matricula)
                ilike("matricula", args.matricula + '%')

        }
        args.sort = "nome"
        [
            list: Funcionario.createCriteria().list(args, criteria),
            count: Funcionario.createCriteria().count(criteria)
        ]
    }


}