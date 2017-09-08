package com.sysdata.gestaofrota

class PortadorVeiculo extends Portador {

    static belongsTo = [veiculo:Veiculo]

    static constraints = {
    }
}
