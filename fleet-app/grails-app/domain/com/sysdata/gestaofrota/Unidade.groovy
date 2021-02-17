package com.sysdata.gestaofrota

class Unidade {

    String codigo
    String nome
    Status status = Status.ATIVO
    Date dateCreated
    String nomeEmbossing

    static belongsTo = [rh: Rh]

    static hasMany = [funcionarios: Funcionario, veiculos: Veiculo, portadores: Portador]

    static transients = ['funcionariosCount','veiculosCount']

    static constraints = {
        nome nullable: false, blank: false
        nomeEmbossing nullable: false
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'unidade_seq']
        nomeEmbossing size: 50
    }

    static hibernateFilters = {
        unidadePorRh(condition: 'rh_id=:rh_id', types: 'long')

    }

    @Override
    String toString() {
        return "${this.codigo} - ${this.nome}"
    }

/*
    def getFuncionariosCount(){
        Funcionario.countByUnidade(this)?:0
    }

    def getVeiculosCount(){
        Veiculo.countByUnidade(this)?:0
    }
*/

}