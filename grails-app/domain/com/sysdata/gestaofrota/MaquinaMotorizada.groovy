package com.sysdata.gestaofrota

abstract class MaquinaMotorizada {

    Long capacidadeTanque
    TipoAbastecimento tipoAbastecimento
    Date dateCreated

    static auditable = true

    static hasOne = [portador: PortadorMaquina]

    static belongsTo = [unidade: Unidade]

    static hasMany = [funcionarios: MaquinaFuncionario]

    static constraints = {
        dateCreated nullable: true
        portador nullable: true
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'maquina_seq']
    }
}
