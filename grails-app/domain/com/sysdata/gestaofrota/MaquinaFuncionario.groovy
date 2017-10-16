package com.sysdata.gestaofrota

class MaquinaFuncionario {

    static belongsTo = [maquina: MaquinaMotorizada, funcionario: Funcionario]

    Date dateCreated
    Status status = Status.ATIVO

    static mapping = {
        id generator: "sequence", params: [sequence: "maqfunc_seq"]
    }
}
