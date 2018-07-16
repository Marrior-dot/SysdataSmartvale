package com.sysdata.gestaofrota

import java.text.SimpleDateFormat
import java.util.Date;

import com.sysdata.gestaofrota.exception.ArquivoException

import groovy.text.GStringTemplateEngine

import com.sysdata.gestaofrota.Util

class GeracaoCartaSenhaService {

	static transactional=true
	
	private String FILE_TEMPLATE=
	"""<%
		out<<String.format('CS10603%s%sAMAZONCARD%-50s\\n',date,lote,"")
	
	    cardsList.each { 
		     def beneficio=it.funcionario.unidade.rh.nome.size()<=25?it.funcionario.unidade.rh.nome:it.funcionario.unidade.rh.nome.substring(0,25) 
		     def cep=(it.funcionario.endereco?.cep)?it.funcionario.endereco?.cep.replace('-',''):'' 
		     def cidade=(it.funcionario.endereco?.cidade?.nome)?:'' 
		     def uf=(it.funcionario.endereco?.cidade?.estado?.uf)?:''
			 def matric=(it.funcionario.matricula?Util.clearMask(it.funcionario.matricula) as int:0
 
		     out<<String.format('CS2%19s%-30s%-25s%-60s%-20s%-8s%-20s%-2s%4s%09d\\n',it.numero,it.funcionario.nome,beneficio,it.funcionario.endereco?.logradouro,it.funcionario.endereco?.bairro,cep,cidade,uf,it.senha,matric)}%>"""
	
    def gerarArquivo(unidadeInstance) {
		
        def cardsList=Cartao.withCriteria{
                            funcionario{unidade{eq("id",unidadeInstance.id)}}
                            arquivo{
                                eq("status",StatusArquivo.GERADO)
                                eq("tipo",TipoArquivo.EMBOSSING)
                            }
                        }
		
		if(cardsList){

			def qtde=Arquivo.countByTipo(TipoArquivo.CARTA_SENHA)
			def lote=(qtde%1000)+1
			
			def arquivoInstance=new Arquivo(nome:String.format("%s%04d%04d%03d.SEN",new SimpleDateFormat("yyMMdd").format(new Date()),	unidadeInstance.rh.codigo as int,unidadeInstance.id,lote),
											tipo:TipoArquivo.CARTA_SENHA,
											status:StatusArquivo.PROCESSANDO)
			
			def engine=new GStringTemplateEngine()
			
			def binding=[
			             "date":new SimpleDateFormat("ddMMyy").format(new Date()),
			             "lote":String.format("%03d",lote),
			             "cardsList":cardsList
			             ]
			 
			def template=engine.createTemplate(FILE_TEMPLATE).make(binding)
			 arquivoInstance.conteudoText=template.toString()
			 arquivoInstance.status=StatusArquivo.GERADO
			 if(!arquivoInstance.save(flush:true)){
				 arquivoInstance.errors.allErrors.each{
					 log.error "Atributo:${it.field} Valor Rejeitado:${it.rejectedValue}"
				 }
				 throw new ArquivoException(message:"Erro ao salvar arquivo")
			 }
 
			 // Obtem os arquivos de embossing que possuem cartoes para emissao de senha e altera o seu status para PROCESSADO
			 def arquivosEmbossing=cardsList.collect{it.arquivo?.id} as Set
			 
			 arquivosEmbossing.each{
				 def arqEmb=Arquivo.get(it)
				 arqEmb.status=StatusArquivo.PROCESSADO
			 }
				
		}
		
		cardsList
		
    }
}
