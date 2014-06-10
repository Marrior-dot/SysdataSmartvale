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
	
	static mapping={
		id generator:'sequence',params:[sequence:'transacao_seq']
		version false
	}
}
