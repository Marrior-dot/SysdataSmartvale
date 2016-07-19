package com.sysdata.gestaofrota

class Lancamento {

	Double valor
	Date dateCreated
	Date dataEfetivacao
	TipoLancamento tipo
	StatusLancamento status
    String referencia
	
	static belongsTo=[transacao:Transacao,conta:Conta]
	
    static constraints = {
        dataEfetivacao nullable: true
        referencia nullable: true
    }
}
