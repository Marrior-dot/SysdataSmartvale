package com.sysdata.gestaofrota

class Transacao {

	Double valor
	Date dateCreated
	Date dataHora
	StatusTransacao status
	Integer nsu
	String numeroCartao
	String codigoEstabelecimento
	String terminal
	Integer nsuTerminal
	String codigoRetorno
	String placa
	StatusControleAutorizacao statusControle
	TipoTransacao tipo
	Cartao cartao
	Estabelecimento estabelecimento
	Boolean autorizada
	TipoCombustivel combustivel
	Long quilometragem
	Double precoUnitario
	MotivoNegacao motivoNegacao
	MaquinaMotorizada maquina
	String codigoEquipamento
	
	static belongsTo=[participante:Participante]
	
	static hasMany=[lancamentos:Lancamento,produtos:TransacaoProduto]
	
    static constraints = {
		dataHora(nullable:true)
		placa(nullable:true)
		nsu(nullable:true)
		numeroCartao(nullable:true)
		codigoEstabelecimento(nullable:true)
		nsuTerminal(nullable:true)
		codigoRetorno(nullable:true)
		participante(nullable:true)
		estabelecimento(nullable:true)
		cartao(nullable:true)
		terminal(nullable:true)
		autorizada(nullable:true)
		combustivel(nullable:true)
		quilometragem(nullable:true)
		precoUnitario(nullable:true)
		statusControle(nullable:true)
		motivoNegacao(nullable:true)
		maquina nullable:true
		codigoEquipamento nullable:true
    }
	
	
	Conta getConta(){
		this.cartao?.funcionario?.conta
	}
	
	
	static mapping={
		id generator:'sequence',params:[sequence:'transacao_seq']
		version false
	}
	
	static hibernateFilters = {
		// Filtros Transacao
		transacaoPorEstabelecimento(condition:'estabelecimento_id=:estabelecimento_id',types:'long')
		
		transacaoPorParticipante(condition:'participante_id=:participante_id',types:'long')
		
		transacaoPorRH(condition:'participante_id in (select f.id from Participante f where f.unidade_id in (select u.id from Unidade u where u.rh_id=:rh_id))',types:'long')
		
		transacaoPorPosto(condition: 'estabelecimento_id in (select e.id from Participante e where e.empresa_id=:posto_id)', types: 'long')
	}
}
