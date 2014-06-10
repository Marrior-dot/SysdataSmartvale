package com.sysdata.gestaofrota

class Lancamento {

	Double valor
	Date dateCreated
	Date dataEfetivacao
	TipoLancamento tipo
	StatusLancamento status
	
	static belongsTo=[transacao:Transacao,conta:Conta]
	
    static constraints = {
    }
}
