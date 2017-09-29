package com.sysdata.gestaofrota

class Participante {
    Conta conta = new Conta()
    String nome
    Endereco endereco
    Telefone telefone
    Status status = Status.ATIVO
    Date dateCreated
    DadoBancario dadoBancario

    static auditable = true

    static embedded = ['endereco', 'telefone', 'dadoBancario']

    static constraints = {
        endereco(nullable: true)
        telefone(nullable: true)
        dadoBancario(nullable: true)
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'participante_seq']
        conta lazy: false
    }

    static hibernateFilters = {
        empresaPorUser(condition: 'id=:owner_id', types: 'int')
        funcionariosPorUnidade(condition: 'unidade_id=:unidade_id', types: 'long')
        estabelecimentoPorPosto(condition: '(empresa_id=:empresa_id or id=:empresa_id)', types: 'long')
    }

}
