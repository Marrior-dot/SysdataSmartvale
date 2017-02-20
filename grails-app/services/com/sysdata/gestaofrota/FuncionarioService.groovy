package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.FuncionarioException

class FuncionarioService extends ParticipanteService{

    static transactional=false

    def geradorCartao

    synchronized def gerarCartao(funcionarioInstance) {

		Cartao cartaoInstance=new Cartao()
		cartaoInstance.numero=geradorCartao.gerarNumero(funcionarioInstance)
		cartaoInstance.funcionario=funcionarioInstance
		cartaoInstance.senha=geradorCartao.gerarSenha()

		Calendar cal=Calendar.getInstance()
		cal.setTime(new Date())
		def anosValidade=ParametroSistema.getValorAsInteger(ParametroSistema.ANOS_VALIDADE_CARTAO)
		cal.add(Calendar.YEAR, anosValidade)

		cartaoInstance.validade=cal.getTime()

		if(!cartaoInstance.save(flush:true)){
			cartaoInstance.errors.allErrors.each {
				log.error it
			}
			throw new FuncionarioException(message:"Erro ao criar novo cartão")
		}
		true
    }
	
	def save(funcionarioInstance){
		saveCidade(funcionarioInstance.endereco)
		return funcionarioInstance.save(flush:true)
	}
	
}
