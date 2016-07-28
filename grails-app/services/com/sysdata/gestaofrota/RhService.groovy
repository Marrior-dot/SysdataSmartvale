package com.sysdata.gestaofrota

class RhService {

	def participanteService
	
    synchronized def saveNew(rhInstance) {
		def ultCod=Rh.withCriteria(uniqueResult:true) {
			projections{max('id')}
		}

		ultCod = Integer.parseInt(Rh.get(ultCod).codigo)
		
		def novoCod=ultCod?++(ultCod as long):1
		
		rhInstance.codigo=novoCod
		
		participanteService.saveCidade(rhInstance.endereco)
		rhInstance.save(flush:true,failOnError: true)
    }
}
