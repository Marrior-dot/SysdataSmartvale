package com.sysdata.gestaofrota

class PostoCombustivelService extends ParticipanteService{

    static transactional=false

	def save(postoCombustivelInstance){
		saveCidade(postoCombustivelInstance.endereco)
		postoCombustivelInstance.save(flush:true)
	}
	
		
	synchronized gerarCodigo(postoCombustivelInstance){
		def ultCod=Estabelecimento.withCriteria{
			projections{max('codigo')}
		}[0]?:0

		ultCod=ultCod.toLong()
		def novoCodEstab=String.format("%015d",++ultCod)
		log.debug("Novo Cod Estab:${novoCodEstab}")
			
		postoCombustivelInstance.addToEstabelecimentos(new Estabelecimento(codigo:novoCodEstab))
		
		return postoCombustivelInstance.save(flush:true)

	}
}
