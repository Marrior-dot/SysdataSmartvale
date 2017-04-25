package com.sysdata.gestaofrota

import grails.plugins.springsecurity.Secured;

import java.text.SimpleDateFormat
import org.jpos.iso.ISOChannel
import org.jpos.iso.ISOMsg
import org.jpos.iso.channel.ASCIIChannel
import org.jpos.iso.packager.ISO87APackager;

import com.sysdata.gestaofrota.ca.NsuTermFile;

class FuelTransactionCommand{
	
	String cartao
	String matricula
	String placa
	String quilometragem
	String estabelecimento
	TipoCombustivel tipoCombustivel
	String valor
	String senha
	String vencimento
	
	String nsuTerminal
	String nsuHost
	String dataHost
	String horaHost
	boolean autorizada
	String codigoRetorno
	
	String toString(){
		def flat=""
		this.properties.each{k,v->
			flat+="$k:$v\n"
		}
		flat
	}
}

@Secured(['IS_AUTHENTICATED_FULLY'])
class CentralAtendimentoController {
	
	def authServerService
	def funcionarioService
	def springSecurityService
		
    def index = { }
	
	def searchCard={
		[act:params.act,goTo:params.goTo]
	}
	
	def findFuncionario={
		flash.errors=[]
		def numero=params.cartao
		if(numero){
			def cartaoInstance=Cartao.findByNumero(numero)
			def participante = cartaoInstance.funcionario
			if(cartaoInstance){
				render(view:'manageCard',model:[cartaoInstance:cartaoInstance,goTo:params.goTo,participante:participante])
			}else{
				flash.errors<<"Nenhum cartão localizado com este número"
				render(view:'searchCard',model:[act:'findFuncionario',goTo:params.goTo])
			}
		}else{
			flash.errors<<"Nº de cartão nulo"
			render(view:'searchCard',model:[act:'findFuncionario',goTo:params.goTo])
		}
	}
	
	def unlockNewCard={
		flash.errors=[]
		def cartaoInstance=Cartao.get(params.id)
		def participante = cartaoInstance.funcionario
		if(cartaoInstance){
			cartaoInstance.status=StatusCartao.ATIVO
			if(cartaoInstance.save(flush:true)){
				log.info "User:${springSecurityService.currentUser?.name}-Cartao ${cartaoInstance.numero} desbloqueado"
				flash.message="Cartão DESBLOQUEADO com sucesso"
			}
			else
				flash.errors<<"Erro ao Desbloquear Cartão"
			[cartaoInstance:cartaoInstance]
		}
		render view:'manageCard',model:[cartaoInstance:cartaoInstance,participante:participante]
	}
	
	/* Ao cancelar um determinado cartão, um novo é automaticamente gerado */
	def cancelCard={
		println "params : $params"
		flash.errors=[]
		def cartaoInstance=Cartao.get(params.id)
		def participante = cartaoInstance.funcionario
		if(cartaoInstance){
			//println "motivoCancelamento ${MotivoCancelamento.valueOf(params.motivoCancelamento)}"
				if((params.motivoCancelamento as MotivoCancelamento) != MotivoCancelamento.SOLICITACAO_ADM){
					cartaoInstance.status=StatusCartao.CANCELADO
					cartaoInstance.motivoCancelamento = MotivoCancelamento.valueOf(params.motivoCancelamento)
					println "É diferente de solicitacao_adm"
					funcionarioService.gerarCartao(cartaoInstance.funcionario)

				}else{
					println "É igual a solicitacao_adm"
					cartaoInstance.status=StatusCartao.CANCELADO
					cartaoInstance.motivoCancelamento = MotivoCancelamento.valueOf(params.motivoCancelamento)
					cartaoInstance.funcionario.status = Status.BLOQUEADO
					Participante.executeUpdate("""update Participante p set p.status='BLOQUEADO' where p.id=:parId""",[parId:participante.id])
				}
				if(cartaoInstance.save(flush: true, failOnError: true)){
					//cartaoInstance.save(flush: true, failOnError: true) &&
					println "Salvou o cartao"
					log.info "User:${springSecurityService.currentUser?.name}-Cartao ${cartaoInstance.numero} cancelado"
					flash.message = "Cartão CANCELADO com sucesso, pelo motivo ${cartaoInstance.motivoCancelamento}"

				}else{
				cartaoInstance.errors.allErrors.each {
					log.error it
				}
				flash.errors<<"Erro ao Cancelar Cartão!"
				}

				//[cartaoInstance:cartaoInstance]
			//funcionarioInstance.save(flush: true, failOnError: true)
			//println "Salvou o funcionario"

		}
		println "cartaoInstance.status: ${cartaoInstance.funcionario.status}"
		render view:'manageCard',model:[cartaoInstance:cartaoInstance, participante:participante]
	}
	
	
	def settingPriceTransaction={}
	def fuelTransaction={}
	
	def doSettingPriceTransaction={
		flash.errors=[]
		if(params.estabelecimento){
			if(params.preco){
				try {
					def ret=authServerService.handleSettingPriceTransaction(params)
					if(ret['autorizada'])
						flash.message="Transação de Alteração de Preço do Combustível APROVADA. Nsu Aut:${}"
					else
						flash.error<<"Transação NEGADA. Cod Retorno:${ret['codresp']}"
				}catch(IOException e){
					log.error e
					flash.errors<<"Erro na conexão ao Autorizador. Contate suporte"
				}
				
			}else
				flash.message="Preço não pode ser nulo"
		}else
			flash.message="Estabelecimento não pode ser nulo"
		render(view:"settingPriceTransaction")
	}	
	
	def doFuelTransaction={FuelTransactionCommand cmd->
		flash.errors=[]
		def resp
		try{
			resp=authServerService.handleFuelTransaction(cmd)
			if(resp.autorizada)
				flash.message="Transação AUTORIZADA. Codigo Autorização:${resp.nsuHost}"
			else
				flash.errors<<"Transação NEGADA. Cod Retorno: "+resp.codigoRetorno
			
		}catch(Exception e){
			log.error e
			flash.errors<<"Falha na comunicação com o Autorizador. Contate suporte "
		}
		
		render(view:"fuelTransaction",model:[commandInstance:cmd])
	}
	
}
