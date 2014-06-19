package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.TransacaoException

class EstornoService {

    def estornarTransacao(transacaoInstance) {

		log.info "Estornando transacao ${transacaoInstance.id} ..."
				
		if(transacaoInstance){
			
			if(transacaoInstance.tipo in [TipoTransacao.COMBUSTIVEL, TipoTransacao.SERVICOS] && 
			   transacaoInstance.status in [StatusTransacao.AGENDAR, StatusTransacao.AGENDADA] &&
			   transacaoInstance.statusControle==StatusControleAutorizacao.CONFIRMADA){
	
					log.debug "Verificando se reembolso ja foi realizado..."
					
					if(! transacaoInstance.lancamentos.find{it.tipo==TipoLancamento.REEMBOLSO && it.status==StatusLancamento.EFETIVADO}){
						
						transacaoInstance.status=StatusTransacao.ESTORNADA
						
						transacaoInstance.lancamentos.each{l->
							l.status=StatusLancamento.ESTORNADO
							log.debug "Status do lancamento ${l.id} alterado para ESTORNADO"
						}
						
						def contaInstance=transacaoInstance.conta
						
						log.debug "Recompondo Saldo Conta (Ant: ${contaInstance.saldo})"
						contaInstance.updateSaldo(transacaoInstance.valor)
						log.debug "Novo Saldo: ${contaInstance.saldo}"
						contaInstance.save(flush:true)
						
						transacaoInstance.save(flush:true)
						
						
				}else
					throw new TransacaoException("Transacao nao pode ser estornada, pois ja foi realizado Reembolso para Lojista")
				
			}else
				throw new TransacaoException("Transacao invalida. Nao pode ser estornada [tp:${transactionInstance.tipo} sts:{transactionInstance.status} stsctrl:${transactionInstance.statusControle}]")
			
		}else
			throw new TransacaoException("Transacao nao pode ser nula para estorno")
		
		log.info "Transacao ${transacaoInstance.id} estornada com sucesso"

    }
}
