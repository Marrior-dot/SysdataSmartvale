package com.sysdata.gestaofrota

class CorteEstabelecimento extends Corte {

    TipoCorteEstabelecimento tipoCorte

    static hasMany = [datasCortadas: Date, pagamentos: PagamentoEstabelecimento]

    static constraints = {
    }
}
