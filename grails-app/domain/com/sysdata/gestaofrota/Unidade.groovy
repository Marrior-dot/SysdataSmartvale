package com.sysdata.gestaofrota

class Unidade {

    String codigo
    String nome
    Status status = Status.ATIVO
    Date dateCreated

    static belongsTo = [rh: Rh]

    static hasMany = [funcionarios: Funcionario, veiculos: Veiculo, portadores: Portador]

    static constraints = {
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'unidade_seq']
    }

    static hibernateFilters = {
        unidadePorRh(condition: 'rh_id=:rh_id', types: 'long')

    }

    @Override
    String toString() {
        return "${rh?.nome} - ${rh?.unidades?.nome}"
    }
}