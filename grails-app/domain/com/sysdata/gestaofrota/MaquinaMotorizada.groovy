package com.sysdata.gestaofrota

abstract class MaquinaMotorizada {
    Long capacidadeTanque
    TipoAbastecimento tipoAbastecimento
    Date dateCreated
    String complementoEmbossing
    Status status

    static auditable = true

    static hasOne = [portador: PortadorMaquina]
    static belongsTo = [unidade: Unidade]
    static hasMany = [funcionarios: MaquinaFuncionario]

    static constraints = {
        dateCreated nullable: true
        portador nullable: true
        complementoEmbossing nullable: true
        status nullable:true
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'maquina_seq']
    }
    static transients = ['nomeEmbossing']

    @Override
    String toString() {
        getNomeEmbossing()
    }

    abstract String getNomeEmbossing();
}
