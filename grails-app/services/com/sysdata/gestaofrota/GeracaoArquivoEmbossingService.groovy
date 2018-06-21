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

/*        if (cartoesComChip.size() > 0) {
            println "Qtd cartoes com chip: ${cartoesComChip.size()}"
            embossadora = new PaySmart(cartoesComChip)
            Arquivo arquivo = embossadora.gerar()

            println("Cartoes com chip: ")
            println(arquivo.conteudo)

//            if (!arquivo.save(flush: true)) {
//                arquivo.errors.allErrors.each {
//                    log.error "Atributo: ${it.field} Valor Rejeitado: ${it.rejectedValue}"
                      return false
//                }
//                throw new ArquivoException(message: "Erro ao salvar arquivo PaySmart (cartões com chip).")
//            }else{
                  return true
              }
        }*/

        if (cartoesSemChip.size() > 0) {
            embossadora = new IntelCav(cartoesSemChip)
            Arquivo arquivo = embossadora.gerar()

            println("Cartoes sem chip: ")
            println(arquivo.conteudo)

            String a = new String(arquivo.conteudo)
            println "a: ${a}"
            println "a string: ${a.toString()}"

            if(arquivo.save(flush: true, failOnError: true)){
                println "Salvou o arquivo. ${arquivo.dump()}"
                String aa = new String(arquivo.conteudo)
                println "conteudo depois de salvo: ${aa}"
            }else{
               println "deu ruim"
               return false
               throw new ArquivoException(message: "Erro ao salvar arquivo IntelCav (cartões sem chip).")
           }
        }
    }
}