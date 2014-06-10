package com.sysdata.gestaofrota

import java.text.SimpleDateFormat

import com.sysdata.gestaofrota.exception.ArquivoException

class GeracaoArquivoEmbossingService {

    static transactional=true

    def gerarArquivo() {
		
		def cartoesEmbossar=Cartao.findAllByStatus(StatusCartao.CRIADO)
		
		if(cartoesEmbossar){
		
			def now=new Date()	
			def arquivoInstance=new Arquivo(nome:String.format("ARQ_EMB_%s.emb",new SimpleDateFormat("yyyyMMddHHmm").format(now)),
											tipo:TipoArquivo.EMBOSSING,
											status:StatusArquivo.PROCESSANDO)
			
			StringBuilder sb=new StringBuilder()
			
			cartoesEmbossar.each{cr->
				sb.append(String.format("""#DCC##EMB#%4s %4s %4s %3s"%-27s"%-27s"#ENC#;%19s=%2s%2s000001060000%1s?#END#@@@@@@""",
										cr.numero.substring(4,8),
										cr.numero.substring(8,12),
										cr.numero.substring(12,16),
										cr.numero.substring(16,19),
										cr.funcionario.nome.size()>27?cr.funcionario.nome.substring(0,27):cr.funcionario.nome,
										cr.funcionario.unidade.rh.nome.size()>27?cr.funcionario.unidade.rh.nome.substring(0,27):cr.funcionario.unidade.rh.nome,
										cr.numero,
										new SimpleDateFormat("yy").format(cr.validade),
										new SimpleDateFormat("MM").format(cr.validade),
										cr.numero.substring(18,19)
										))
				sb.append("\r\n")
				
				cr.status=StatusCartao.EMBOSSING
				cr.arquivo=arquivoInstance

			}
			
			arquivoInstance.conteudo=sb.toString()
			arquivoInstance.status=StatusArquivo.GERADO
			if(!arquivoInstance.save(flush:true)){
				arquivoInstance.errors.allErrors.each{
					log.error "Atributo:${it.field} Valor Rejeitado:${it.rejectedValue}"
				}
				throw new ArquivoException(message:"Erro ao salvar arquivo")
			}
		}
		cartoesEmbossar 
    }
}
