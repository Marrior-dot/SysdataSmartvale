package com.sysdata.gestaofrota

class Lancamento {

	Double valor
	Date dateCreated
	Date dataEfetivacao
	TipoLancamento tipo
	StatusLancamento status
    String referencia
	StatusFaturamento statusFaturamento

	static transients = ['descricao']
	
	static belongsTo=[transacao:Transacao,conta:Conta]
	
    static constraints = {
        dataEfetivacao nullable: true
        referencia nullable: true
		transacao nullable: true
    }

	String getDescricao(){
        return "${this.tipo.nome} - ${this.conta.participante.nome}"
	}
}
