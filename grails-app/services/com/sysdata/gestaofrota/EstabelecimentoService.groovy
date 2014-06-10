package com.sysdata.gestaofrota

class EstabelecimentoService extends ParticipanteService {

    static transactional=false

	def save(estabelecimentoInstance){
		saveCidade(estabelecimentoInstance.endereco)
		return estabelecimentoInstance.save(flush:true)
	}

	
    def synchronized gerarCodigo(estabelecimentoInstance) {
		def ultCod=Estabelecimento.withCriteria{
			projections{max('codigo')}
		}[0]?:0

		ultCod=ultCod.toLong()
		def novoCodEstab=String.format("%015d",++ultCod)
		log.debug("Novo Cod Estab:${novoCodEstab}")
			
		estabelecimentoInstance.codigo=novoCodEstab
    }
}
