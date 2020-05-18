package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class EstabelecimentoService {

    synchronized def gerarCodigo(estabelecimento) {
        def ultCod = Estabelecimento.executeQuery("select max(cast(e.codigo as int)) from Estabelecimento e")[0] ?: 0
        def novoCodEstab = String.format("%015d", ++ultCod)
        log.debug("Novo Cod Estab:${novoCodEstab}")
        estabelecimento.codigo = novoCodEstab
        estabelecimento
    }
}
