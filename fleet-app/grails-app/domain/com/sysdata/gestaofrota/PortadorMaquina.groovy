package com.sysdata.gestaofrota

class PortadorMaquina extends Portador {

    static belongsTo = [maquina: MaquinaMotorizada]

    static constraints = {
    }
}
