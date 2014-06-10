package com.sysdata.gestaofrota

import java.text.SimpleDateFormat


import org.jpos.iso.ISOChannel
import org.jpos.iso.ISOField
import org.jpos.iso.ISOMsg
import org.jpos.iso.channel.ASCIIChannel
import org.jpos.iso.packager.ISO87APackager
import org.jpos.util.Logger
import org.jpos.util.SimpleLogListener

import com.sysdata.authserver.channel.BASE24TCPChannel
import com.sysdata.authserver.packager.AsciiISOPackager
import com.sysdata.gestaofrota.ca.AuthAccessFile
import com.sysdata.gestaofrota.ca.NsuTermFile

class AuthServerService {
	
	Logger logger
	
	AuthServerService(){
		logger=new Logger()
		logger.addListener(new SimpleLogListener(System.out))
	}

	def withAuth={clos->
		
		def now=new Date()
		ISOChannel channel
		def authIp
		def authPort
		try{
			authIp=AuthAccessFile.getAuthIp()
			authPort=AuthAccessFile.getAuthPort()
			channel=new BASE24TCPChannel(authIp,authPort,new AsciiISOPackager())
			channel.setLogger(logger,"auth-channel")
			channel.connect()
			clos.call(channel,now)
		}catch(Exception e){
			log.error "Falha na comunicacao com o Autorizador (IP:${authIp} PORT:${authPort}) -"+e
			throw new RuntimeException(e)
		}finally{
			if(channel)
				channel.disconnect()
		}
	}
	
	
	def handleSettingPriceTransaction(params){
		
		def ret=[:]
		withAuth{channel,now->
			ISOMsg req=new ISOMsg()
			req.setPackager(new AsciiISOPackager())
			req.setMTI("0800")
			req.set(new ISOField(3,"600000"))
			def preco=Util.decimalMode(Util.convertToCurrency(params.preco.trim()))
			req.set(4,preco.toString())
			req.set(7,new SimpleDateFormat("MMddhhmmss").format(now))
			req.set(11,NsuTermFile.nextNsu().toString())
			req.set(12,new SimpleDateFormat("hhmmss").format(now))
			req.set(13,new SimpleDateFormat("MMdd").format(now))
			req.set(41,AuthAccessFile.getTerminalNumber())
			req.set(42,params.estabelecimento)
			req.set(62,String.format("%02d",1))
			req.set(70,"001")
			
			channel.send(req)
			ISOMsg resp=channel.receive()
			ret=["autorizada":resp.getString(39)=="00","codresp":resp.getString(127)]
		}
		ret
	}
	
	def handleUndoFuelTransaction(params){
		def now=new Date()
		ISOChannel channel
		
		try{
			channel=new ASCIIChannel(AuthAccessFile.getAuthIp(),AuthAccessFile.getAuthPort(),new ISO87APackager())
			channel.connect()
			ISOMsg req=new ISOMsg()
			
			req.setMTI("0420")
			req.set(3,"002100")
			req.set(4,params.valor*100)
			req.set(7,new SimpleDateFormat("MMddhhmmss").format(now))
			//Nsu Terminal da 200
			req.set(11,params.originalNsuTerm)
			req.set(22,"021")
			req.set(39,"86")
			req.set(41,AuthAccessFile.getTerminalNumber())
			req.set(42,params.estabelecimento)
			req.set(48,"022001")
			req.set(49,"076")
			req.set(61,"88844112")
			def dados=String.format("%04d%06d%04d%06d%22d",
									"0200",
									params.originalNsuHost,
									params.originalDate,
									params.originalHour,
									"0")
			req.set(90,dados)
			
			channel.send(req)
			ISOMsg resp=channel.receive()
			
			resp

	
		}finally{
			if(channel)
				channel.disconnect()
		}


	}
	
	def handleConfirmFuelTransaction(){
		def now=new Date()
		ISOChannel channel
		
		try{
			channel=new ASCIIChannel(AuthAccessFile.getAuthIp(),AuthAccessFile.getAuthPort(),new ISO87APackager())
			channel.connect()
			ISOMsg req=new ISOMsg()
			
			req.setMTI("0202")
			req.set(3,"002100")
			req.set(4,params.valor*100)
			req.set(7,new SimpleDateFormat("MMddhhmmss",now))
			//Nsu Terminal da 200
			req.set(11,params.originalNsuTerm) 
			req.set(41,AuthAccessFile.getTerminalNumber())
			req.set(42,params.estabelecimento)
			req.set(49,"076")
			//Nsu Host da 200
			req.set(127,params.originalNsuHost)
			
			channel.send(req)
			ISOMsg resp=channel.receive()
			
			resp

	
		}finally{
			if(channel)
				channel.disconnect()
		}

	}
	
	
    def handleFuelTransaction(commandInstance) {
		
		withAuth{channel,now->
			ISOMsg req=new ISOMsg()
			req.setMTI("0200")
			req.set(2,commandInstance.cartao)
			req.set(3,"002100")
			def val=Util.decimalMode(Util.convertToCurrency(commandInstance.valor.trim()))
			req.set(4,val.toString())
			req.set(7,new SimpleDateFormat("MMddHHmmss").format(now))
			req.set(11,NsuTermFile.nextNsu().toString())
			req.set(12,new SimpleDateFormat("HHmmss").format(now))
			req.set(13,new SimpleDateFormat("MMdd").format(now))
			req.set(14,commandInstance.vencimento)
			req.set(22,"011")
			req.set(41,AuthAccessFile.getTerminalNumber())
			req.set(42,commandInstance.estabelecimento)
			req.set(48,"22001")
			req.set(49,"076")
			def bpsw=commandInstance.senha.bytes
			req.set(52,commandInstance.senha.bytes)
			req.set(61,"8884")
			def dados=String.format("%012d%04d%09d%02d",commandInstance.matricula?commandInstance.matricula.replaceAll("-","").toInteger():0,
														commandInstance.placa.substring(4).toInteger(),
														commandInstance.quilometragem.toInteger(),
														commandInstance.tipoCombustivel.ordinal()+1+1)
			
			req.set(62,dados)
			req.set(63,"0")
			
			channel.send(req)
			
			ISOMsg resp=channel.receive()
			
			if(resp.hasField(39)){
				commandInstance.codigoRetorno=resp.getString(39)
				if(resp.getString(39)=="00"){
					commandInstance.nsuTerminal=resp.getString("11")
					commandInstance.nsuHost=resp.getString("127")
					commandInstance.dataHost=resp.getString("12")
					commandInstance.horaHost=resp.getString("13")
					commandInstance.autorizada=true
				}else 
					commandInstance.autorizada=false
			}else{
				throw new RuntimeException("Falha ao realizar transação. Não há código de retorno")
			}

			
		}

		commandInstance

    }
}
