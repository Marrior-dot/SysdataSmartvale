package com.sysdata.gestaofrota

class Lancamento {

	Double valor
	Date dateCreated
	Date dataEfetivacao
	TipoLancamento tipo
	StatusLancamento status
	StatusFaturamento statusFaturamento = StatusFaturamento.NAO_FATURADO

	static transients = ['descricao']
	
	static belongsTo=[transacao:Transacao,conta:Conta]
	
    static constraints = {
        dataEfetivacao nullable: true
		transacao nullable: true
    }

	String getDescricao(){
        return "${this.tipo.nome} - ${this.conta.participante.nome}"
	}
}
