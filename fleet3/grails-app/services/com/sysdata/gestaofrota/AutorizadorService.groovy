package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.TransacaoException

class AutorizadorService {

    static transactional=false
	
	def regras=[
				[codRetorno:"01-Cartão inexistente",regra:{tr->tr.cartao!=null}],
				[codRetorno:"02-Cartão não ativo",regra:{tr->tr.cartao.status==StatusCartao.ATIVO}],
				[codRetorno:"03-Estabelecimento inexistente",regra:{tr->tr.estabelecimento!=null}],
				[codRetorno:"04-Estabelecimento inativo",regra:{tr->tr.estabelecimento.posto.status==Status.ATIVO}],
				[codRetorno:"05-Veículo Inexistente",regra:{tr->tr.veiculo!=null}],
				[codRetorno:"06-Veículo não relacionado ao Funcionário",regra:{tr->tr.veiculo.funcionarios.find{it.funcionario==tr.cartao.funcionario}}],
				[codRetorno:"07-Tipo de Combustível incompatível com veículo",
					regra:{tr->
						(tr.veiculo.tipoAbastecimento==TipoAbastecimento.BICOMBUSTIVEL && tr.combustivel in ["Gasolina","Álcool"])||(tr.veiculo.tipoAbastecimento.nome==tr.combustivel)

					}],
				[codRetorno:"08-Saldo insuficiente",regra:{tr->
					tr.cartao.funcionario.conta.saldo>=tr.valor
					}],
				[codRetorno:"09-Abastecimento superior a capacidade do tanque",
					regra:{tr->
						def litros=tr.valor/tr.precoUnitario
						litros<=tr.veiculo.capacidadeTanque
					}],
				[codRetorno:"10-Quilometragem igual ou inferior a informada anteriormente",
					regra:{tr->
						tr.quilometragem>tr.ultimaQuilometragem
					}],
				[codRetorno:"11-Abastecimento superior a autonomia média do veículo",
					regra:{tr->
						def percorrido=tr.quilometragem-tr.ultimaQuilometragem
						def ideal=tr.veiculo.autonomia*tr.veiculo.capacidadeTanque
						percorrido>=ideal
					}],

				]

    def autorizar(transacao) {
		
		transacao.veiculo=Veiculo.findByPlaca(transacao.placa)
		transacao.cartao=Cartao.findByNumero(transacao.numeroCartao)
		transacao.estabelecimento=Estabelecimento.findByCodigo(transacao.codigoEstabelecimento)
		
		//Obtem ultimo abastecimento do veículo
		def ultAbastId=Abastecimento.withCriteria{
			veiculo{eq("id",transacao.veiculo.id)}
			eq("autorizada",true)
			projections{max("id")}
		}[0]
		def ultMetragem=0
		if(ultAbastId){
			def abastInstance=Abastecimento.get(ultAbastId)
			ultMetragem=abastInstance.quilometragem
		}
		transacao.ultimaQuilometragem=ultMetragem
		
		def msg
		
		for(r in regras){
			def ok=r.regra(transacao)
			if(!ok){
				msg=r.codRetorno
				break
			}
		}
		
		
		def nsu=Abastecimento.withCriteria{
					projections{max('nsu')}
				}[0]
		nsu=nsu?:0
		
		def transacaoInstance=new Transacao(nsu:++nsu,
												placa:transacao.placa,
												valor:transacao.valor,
												numeroCartao:transacao.numeroCartao,
												codigoEstabelecimento:transacao.codigoEstabelecimento,
												veiculo:transacao.veiculo,
												cartao:transacao.cartao,
												codigoRetorno:msg?msg.split("-")[0]:"00",
												autorizada:msg?false:true,
												statusControle:StatusControleAutorizacao.CONFIRMADA,
												status:msg?StatusTransacao.NAO_AGENDAR:StatusTransacao.AGENDAR,
												combustivel:transacao.combustivel,
												quilometragem:transacao.quilometragem,
												precoUnitario:transacao.precoUnitario,
												participante:transacao.cartao?.funcionario
												)
		
		if(!transacaoInstance.save(flush:true)){
			def errors=""
			transacaoInstance.errors.allErrors.each{err->
				log.error err
				errors+="${err.defaultMessage}\n"
			}
			throw new TransacaoException(message:errors)
		}else{
			if(!msg){
				def contaInstance=transacaoInstance.participante.conta
				contaInstance.updateSaldo(-transacaoInstance.valor)
			}
		}
			
		
		[transacaoInstance:transacaoInstance,retorno:msg]
    }
}
