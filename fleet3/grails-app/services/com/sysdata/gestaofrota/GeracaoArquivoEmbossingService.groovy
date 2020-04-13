package com.sysdata.gestaofrota
import com.sysdata.gestaofrota.exception.ArquivoException
import com.sysdata.gestaofrota.processamento.embossadoras.Embossadora
import com.sysdata.gestaofrota.processamento.embossadoras.IntelCav
import com.sysdata.gestaofrota.processamento.embossadoras.PaySmart

class GeracaoArquivoEmbossingService {

    static transactional = true

    boolean gerarArquivo() {

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

        List<Cartao> cartoesComChip = Cartao.createCriteria().list(criteria); cartaoComChip = false
        println "Cartoes com Chip: ${cartoesComChip}"
        List<Cartao> cartoesSemChip = Cartao.createCriteria().list(criteria)
        println "Cartoes sem Chip: ${cartoesSemChip}"
        Embossadora embossadora = null

      if (cartoesComChip.size() > 0) {
            println "Qtd cartoes com chip: ${cartoesComChip.size()}"
            embossadora = new PaySmart(cartoesComChip)
            Arquivo arquivo = embossadora.gerar()

            println("Cartoes com chip: ")
            println(arquivo.conteudoText)

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
            embossadora = new IntelCav(cartoesSemChip)
            Arquivo arquivo = embossadora.gerar()
            println("Cartoes sem chip: ")
            println(arquivo.conteudoText)

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