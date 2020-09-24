package com.sysdata.gestaofrota

class Participante {
    Conta conta = new Conta()
    String nome
    Endereco endereco
    Telefone telefone
    Status status = Status.ATIVO
    Date dateCreated = new Date()
    DadoBancario dadoBancario
    String email

    static hasMany = [propriedades: Propriedade]

    static auditable = true

    static embedded = ['endereco', 'telefone', 'dadoBancario']

    static constraints = {
        endereco(nullable: true)
        telefone(nullable: true)
        dadoBancario(nullable: true)
        email nullable: true, email: true
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'participante_seq']
        conta lazy: false
    }

    static hibernateFilters = {
        estabelecimentoPorPosto(condition: '(empresa_id=:empresa_id or id=:empresa_id)', types: 'long')
    }

}
