package com.sysdata.gestaofrota

class Lancamento {

    Date dateCreated
    Date dataEfetivacao
    TipoLancamento tipo
    StatusLancamento status
    BigDecimal valor

    static transients = ['descricao']

    static belongsTo = [transacao: Transacao, conta: Conta]

    static constraints = {
        dataEfetivacao nullable: true
        transacao nullable: true
    }

    static mapping = {
        dataEfetivacao type: 'date'
        id generator: 'sequence', params: ['sequence': 'lancamento_seq']
    }

	static hibernateFilters = {
        lancamentoPorPosto(condition: "transacao_id in (select t.id from Transacao t, Participante e " +
                "where t.estabelecimento_id = e.id and e.empresa_id = :emp_id)", types: 'long')
    }

    String getDescricao() {
        return "${this.tipo.nome} - ${this.conta.participante.nome}"
    }
}
