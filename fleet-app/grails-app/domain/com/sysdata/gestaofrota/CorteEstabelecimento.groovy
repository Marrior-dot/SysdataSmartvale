package com.sysdata.gestaofrota

/**
 * Representa o agrupamento de todos os Pagamentos a ECs gerados em determinada data previamente programada.
 * Podendo ser para agrupar Pagamentos regulares ou Pagamentos oriundos de Antecipação de Recebíveis.
 * Os cortes regulares e de antecipação ocorrem em momentos distintos
 */

class CorteEstabelecimento extends Corte {

    TipoCorteEstabelecimento tipoCorte

    static hasMany = [datasCortadas: Date, pagamentos: PagamentoEstabelecimento]

    static constraints = {
    }
}
