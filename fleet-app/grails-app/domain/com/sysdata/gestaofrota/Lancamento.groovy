package com.sysdata.gestaofrota

class Lancamento {

    Double valor
    Date dateCreated
    Date dataEfetivacao
    TipoLancamento tipo
    StatusLancamento status
    StatusFaturamento statusFaturamento = StatusFaturamento.NAO_FATURADO

    static transients = ['descricao']

    static belongsTo = [transacao: Transacao, conta: Conta]

    static constraints = {
        dataEfetivacao nullable: true
        transacao nullable: true
    }

    static mapping = {
        dataEfetivacao type: 'date'
    }


/*
	static hibernateFilters = {
		lancamentoPorPosto(condition: 'transacao in (select t.id from Transacao t, Participante e where t.estabelecimento_id = e.id and e.empresa_id = :emp_id', type: 'long')
	}
*/


    String getDescricao() {
        return "${this.tipo.nome} - ${this.conta.participante.nome}"
    }
}
