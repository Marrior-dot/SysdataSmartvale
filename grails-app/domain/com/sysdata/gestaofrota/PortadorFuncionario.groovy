package com.sysdata.gestaofrota

class PortadorFuncionario extends Portador {

    static belongsTo = [funcionario:Funcionario]

    static constraints = {
    }
}
