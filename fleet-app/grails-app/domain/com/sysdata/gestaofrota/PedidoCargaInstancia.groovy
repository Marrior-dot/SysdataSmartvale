package com.sysdata.gestaofrota

class PedidoCargaInstancia extends PedidoCarga {

    Date dataCarga
    StatusPedidoCarga status = StatusPedidoCarga.NOVO

    static constraints = {
    }
}
