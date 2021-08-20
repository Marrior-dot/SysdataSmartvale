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
        propriedades cascade: 'all-delete-orphan'
    }

    static hibernateFilters = {
        estabelecimentoPorPosto(condition: '(empresa_id=:emp_id or id=:emp_id)', types: 'long')
    }

}
