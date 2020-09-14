package com.sysdata.gestaofrota

class CorteEstabelecimento extends Corte {

    static hasMany = [datasCortadas: Date, pagamentos: PagamentoEstabelecimento]

    static constraints = {
    }
}
