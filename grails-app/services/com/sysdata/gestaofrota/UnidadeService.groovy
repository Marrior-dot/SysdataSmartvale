package com.sysdata.gestaofrota

class UnidadeService {

    synchronized def saveNew(unidadeInstance) {
		def ultCod=Unidade.withCriteria(uniqueResult:true) {
						projections{max("codigo")}
					}
	
		def novoCod=ultCod?++(ultCod as long):1
		
		unidadeInstance.codigo=novoCod
		unidadeInstance.save(flush:true)

    }
}
