package com.sysdata.gestaofrota

class PortadorAnonimo extends Portador {

    static constraints = {
    }

    static PortadorAnonimo getUnico() {
        PortadorAnonimo portadorAnonimo
        if (PortadorAnonimo.count() == 0) {
            portadorAnonimo = new PortadorAnonimo()
            portadorAnonimo.save(flush: true)
        } else
            portadorAnonimo = PortadorAnonimo.first()

        return portadorAnonimo
    }
}