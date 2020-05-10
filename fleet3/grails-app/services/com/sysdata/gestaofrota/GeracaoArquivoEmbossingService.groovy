package com.sysdata.gestaofrota
import com.sysdata.gestaofrota.exception.ArquivoException
import com.sysdata.gestaofrota.processamento.embossadoras.Embossadora
import com.sysdata.gestaofrota.processamento.embossadoras.IntelCav
import com.sysdata.gestaofrota.processamento.embossadoras.PaySmart

class GeracaoArquivoEmbossingService {

    static transactional = true

    boolean gerarArquivo() {

        log.info "Iniciando Geração Embossing..."
        boolean cartaoComChip = true
        Closure criteria = {
            eq('status', StatusCartao.CRIADO)
            portador {
                unidade {
                    rh {
                        eq('cartaoComChip', cartaoComChip)
                    }
                }
            }
        }

        List<Cartao> cartoesComChip = Cartao.createCriteria().list(criteria);
        cartaoComChip = false
        List<Cartao> cartoesSemChip = Cartao.createCriteria().list(criteria)
        Embossadora embossadora = null

      if (cartoesComChip.size() > 0) {
            log.info "Qtde com chip: ${cartoesComChip.size()}"
            embossadora = new PaySmart(cartoesComChip)
            Arquivo arquivo = embossadora.gerar()

            if (arquivo.save(flush: true, failOnError: true)) {
                //File file = new File("/home/diego/${arquivo.nome}")
                //file.withWriter {w ->
                //    w.write(aa)
                //}
                return true
            }else{
                arquivo.errors.allErrors.each {
                    log.error "Atributo: ${it.field} Valor Rejeitado: ${it.rejectedValue}"
                    return false
                }
                throw new ArquivoException(message: "Erro ao salvar arquivo PaySmart (cartões com chip).")
            }
        }

        if (cartoesSemChip.size() > 0) {
            log.info "Qtde com chip: ${cartoesSemChip.size()}"
            embossadora = new IntelCav(cartoesSemChip)
            Arquivo arquivo = embossadora.gerar()

            if(arquivo.save(flush: true, failOnError: true)){
                /*String aa = new String(arquivo.conteudo)
                println "Conteudo depois de salvo: ${aa}"
                File file = new File("/home/diego/${arquivo.nome}")
                file.withWriter {w ->
                    w.write(aa)

                }*/
                return true
            }else{
               return false
               throw new ArquivoException(message: "Erro ao salvar arquivo IntelCav (cartões sem chip).")
           }
        }
    }
}