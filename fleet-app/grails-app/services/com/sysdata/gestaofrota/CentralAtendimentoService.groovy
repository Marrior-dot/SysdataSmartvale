package com.sysdata.gestaofrota

class CentralAtendimentoService {

    def springSecurityService

    private def ret=[:]

    def tranferenciaSaldo(Cartao cartaoCredito, Cartao cartaoDebito,valorTransf){
        def sucesso = false
        println "Gerando transações de transferência de saldo..."

        //Criar transações de transferencia de saldo
        def transacaoDebito = new Transacao(participante:cartaoDebito.funcionario,
                valor:-valorTransf,
                status:StatusTransacao.AGENDAR,
                tipo:TipoTransacao.TRANSFERENCIA_SALDO,
                numeroCartao: cartaoDebito.numero,
                cartao: cartaoDebito,
                dataHora: new Date())


        def transacaoCredito = new Transacao(participante:cartaoCredito.funcionario,
                valor:valorTransf,
                status:StatusTransacao.AGENDAR,
                tipo:TipoTransacao.TRANSFERENCIA_SALDO,
                numeroCartao: cartaoCredito.numero,
                cartao: cartaoCredito,
                dataHora: new Date())

        //Cria instancia de TransferenciaSaldo para ser usado no relatorio depois
        def transferenciaSaldoInstance = new TransferenciaSaldo()
        transferenciaSaldoInstance.valor = valorTransf
        transferenciaSaldoInstance.cartaoReceber = cartaoCredito.numero
        transferenciaSaldoInstance.cartaoTransferir = cartaoDebito.numero
        transferenciaSaldoInstance.dataHora = new Date()
        transferenciaSaldoInstance.usuario=springSecurityService.currentUser

        if(transacaoDebito.save(flush:true) && transacaoCredito.save(flush:true) && transferenciaSaldoInstance.save(flush: true)){

            println "Transacões de Tranferencia de saldo criadas: Cartao $transacaoDebito.numeroCartao com transação de id $transacaoDebito.id - Cartão $transacaoCredito.numeroCartao com transacao de id $transacaoCredito.id"
            if(agendarTransfSaldo(transacaoDebito) && agendarTransfSaldo(transacaoCredito)){
                println "Deu tudo certo ao chamar o Debito"
                sucesso = true
            }else{
                println "Deu erro!"
            }
        }
    return sucesso

    }


    def agendarTransfSaldo(Transacao transacaoInstance) {
        def sucesso = false
        if(transacaoInstance.status==StatusTransacao.AGENDAR){
            println "Gerando lançamentos de transferência de saldo..."
            def contaInstance=transacaoInstance.participante.conta
            def lancamentoInstance=new Lancamento(tipo:TipoLancamento.TRANSFERENCIA_SALDO,
                    status:StatusLancamento.EFETIVADO,
                    valor:transacaoInstance.valor,
                    dataEfetivacao:transacaoInstance.dateCreated,
                    conta:contaInstance)

            transacaoInstance.addToLancamentos(lancamentoInstance)
            //Atualiza saldo conta
            println "Atualiza saldo conta"
            contaInstance.updateSaldo(lancamentoInstance.valor)
            println "Novo saldo: ${contaInstance.saldo}"
            if(contaInstance.save(flush: true) && lancamentoInstance.save(flush: true)){
                ret.ok=true
                println "Lançamentos de Tranferencia de saldo criados: Conta $contaInstance.id. Lancamento id: ${lancamentoInstance.id}"
            }else{
                println "Erro ao salvar conta."
                ret.ok=false
            }

            if(ret.ok){
                transacaoInstance.status=StatusTransacao.AGENDADA

                if(transacaoInstance.save(flush:true)) {
                    sucesso = true

                }else{
                        ret.ok=false
                        println "Deu erro!"
                        transacaoInstance.errors.allErrors.each{err->
                            ret.msg+="TR [${transacaoInstance.id}]: ${err.defaultMessage}\n"
                        }
                }


            }else{
                transacaoInstance.discard()
                println "Discartando transacao"
            }

            return sucesso

        }
    }
}
