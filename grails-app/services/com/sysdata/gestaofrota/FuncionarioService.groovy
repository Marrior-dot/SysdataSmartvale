package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.FuncionarioException

class FuncionarioService extends ParticipanteService{

    static transactional=false

	private String calcularDV(String card) {
		int valor;
		int soma = 0;
		int multiplicador = 1;
		int tamanho = card.length();
		for (int i = 0; i < tamanho; i++) {
			valor = Integer.parseInt(card.substring(i, i + 1)) * multiplicador;
			soma = soma + (valor / 10) + (valor % 10);
			if (multiplicador == 1)
				multiplicador = 2;
			else
				multiplicador = 1;
		}
		return String.valueOf((10 - (soma % 10)) % 10);
	}

	String gerarSenha(){
		Random random=new Random()
		int tamanho=(int)(Math.pow(10,Util.DIGITOS_SENHA)-1)
		int senha=random.nextInt(tamanho)
		String fmt="%0"+Util.DIGITOS_SENHA+"d"
		String.format(fmt,senha)
	}
	
    synchronized def gerarCartao(funcionarioInstance) {
		def binPar=ParametroSistema.findByNome(ParametroSistema.BIN)
		if(binPar){
			def bin=binPar.valor
			def qtde=funcionarioInstance.cartoes?funcionarioInstance.cartoes.size():0
			def cdRh=funcionarioInstance.unidade.rh.id
			def idFunc=funcionarioInstance.id
			def prov=String.format("%6d%1d%03d%06d%02d",bin.toInteger(),4,cdRh.toInteger(),idFunc,++qtde)
			def check=calcularDV(prov)
			Cartao cartaoInstance=new Cartao()
			cartaoInstance.numero=prov+check
			cartaoInstance.funcionario=funcionarioInstance
			cartaoInstance.senha=gerarSenha()
			
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
		}else{
			throw new FuncionarioException(message:"BIN não definido!")
		}
    }
	
	def save(funcionarioInstance){
		saveCidade(funcionarioInstance.endereco)
		return funcionarioInstance.save(flush:true)
	}
	
}
