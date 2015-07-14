package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.TransacaoException

class TransacaoService {

    static transactional=false
	
	private def ret

    def agendarTransacao(transacaoInstance) {
		if(transacaoInstance.status==StatusTransacao.AGENDAR){
			
			if(transacaoInstance.tipo==TipoTransacao.CARGA_SALDO)
				agendarCarga(transacaoInstance)
			if(transacaoInstance.tipo==TipoTransacao.COMBUSTIVEL)
				ret=agendarAbastecimento(transacaoInstance)
				
			if(ret.ok){
				transacaoInstance.status=StatusTransacao.AGENDADA
				
				if(!transacaoInstance.save(flush:true)){
					ret.ok=false
					
					transacaoInstance.errors.allErrors.each{err->
						ret.msg+="TR [${transacaoInstance.id}]: ${err.defaultMessage}\n"
					}
				}
			}else
				transacaoInstance.discard()
		}
    }
	
	def agendarCarga(cargaInstance){
		def contaInstance=cargaInstance.participante.conta
		def lancamentoInstance=new Lancamento(tipo:TipoLancamento.CARGA,
												status:StatusLancamento.EFETIVADO,
												valor:cargaInstance.valor,
												dataEfetivacao:cargaInstance.dateCreated,
												conta:contaInstance)
		cargaInstance.addToLancamentos(lancamentoInstance)
		//Atualiza saldo conta
		contaInstance.updateSaldo(lancamentoInstance.valor)
		contaInstance.save()
	}
	
	
	def calcularDataReembolso(posto,data){
		
		def cal=Calendar.getInstance()
		cal.setTime(data)
		def diaTr=cal.get(Calendar.DAY_OF_MONTH)
		def mesTr=cal.get(Calendar.MONTH)+1
		def anoTr=cal.get(Calendar.YEAR)
		
		def calHoje=Calendar.getInstance()
		calHoje.setTime(new Date())
		def diaHj=calHoje.get(Calendar.DAY_OF_MONTH)+1
		
		
		def reembInstance=posto.reembolsos.find{r->diaTr>=r.inicioIntervalo && diaTr<=r.fimIntervalo}
		
		def dataReembolso
		
		if(reembInstance){
			def diaReemb=reembInstance.diaEfetivacao
			def mesReemb=mesTr+reembInstance.meses
			def anoReemb=anoTr	
		
			if(mesReemb>12){
				mesReemb=1
				anoReemb=anoTr+1
			}
			
			def calReemb=Calendar.getInstance()
			calReemb.set(anoReemb,--mesReemb,diaReemb)
			dataReembolso=calReemb.getTime()
		}
		dataReembolso
	}
	
	def agendarAbastecimento(abastInstance){
		
		//Lancamento funcionario
		def lancCompra=new Lancamento(tipo:TipoLancamento.COMPRA,
										status:StatusLancamento.EFETIVADO,
										valor:abastInstance.valor,
										dataEfetivacao:new Date(),
										conta:abastInstance.participante.conta)
		abastInstance.addToLancamentos(lancCompra)
		//Lancamento estabelecimento
		def estabelecimentoInstance=Estabelecimento.findByCodigo(abastInstance.codigoEstabelecimento)
		double valoReemb=(abastInstance.valor*estabelecimentoInstance.empresa.taxaReembolso/100.0)
		//Arredonda
		def arrend=Util.roundCurrency(valoReemb)
		
		def dataReembolso=calcularDataReembolso(estabelecimentoInstance.empresa,abastInstance.dateCreated)
		
		if(dataReembolso){
			def lancReembolso=new Lancamento(tipo:TipoLancamento.REEMBOLSO,
					status:StatusLancamento.A_EFETIVAR,
					valor:abastInstance.valor-arrend,
					dataEfetivacao:dataReembolso,
					conta:estabelecimentoInstance.empresa.conta)
			abastInstance.addToLancamentos(lancReembolso)
			ret=[ok:true]
		}else{
			ret=[ok:false,msg:"Reembolso não definido para o CNPJ:${estabelecimentoInstance.empresa.cnpj}"]
		}
	}
	
	def agendarAll(){
		
		log.info "Gerando lancamentos financeiros a partir das transacoes..."
		
		def agendarList=Transacao.findAllWhere(status:StatusTransacao.AGENDAR)
		
		agendarList.each{tr->
			agendarTransacao(tr)
			if(ret.ok)
				log.debug "Transacao #${tr.id}  AGENDADA"
			else
				log.debug "Transacao #${tr.id}  NAO AGENDADA - "+ret.msg
		}
		
		log.info agendarList.size()>0?"Lancamentos financeiros gerados":"Nao ha lancamentos financeiros para agendar"
		
		ret
	}
}
