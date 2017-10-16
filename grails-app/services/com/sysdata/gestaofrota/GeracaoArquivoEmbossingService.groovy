package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.ArquivoException
import com.sysdata.gestaofrota.processamento.administradoras.MaxCard
import com.sysdata.gestaofrota.processamento.embossadoras.Embossadora
import com.sysdata.gestaofrota.processamento.embossadoras.IntelCav
import com.sysdata.gestaofrota.processamento.embossadoras.PaySmart

class GeracaoArquivoEmbossingService {

    static transactional = true

    void gerarArquivo() {

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
        List<Cartao> cartoesSemChip = Cartao.createCriteria().list(criteria)
        Embossadora embossadora = null

        if (cartoesComChip.size() > 0) {
            embossadora = new PaySmart(cartoesComChip)
            Arquivo arquivo = embossadora.gerar()

            if (!arquivo.save(flush: true)) {
                arquivo.errors.allErrors.each {
                    log.error "Atributo: ${it.field} Valor Rejeitado: ${it.rejectedValue}"
                }
                throw new ArquivoException(message: "Erro ao salvar arquivo PaySmart (cartões com chip).")
            }
        }

        if (cartoesSemChip.size() > 0) {
            embossadora = new IntelCav(cartoesSemChip)
            Arquivo arquivo = embossadora.gerar()

            if (!arquivo.save(flush: true)) {
                arquivo.errors.allErrors.each {
                    log.error "Atributo: ${it.field} Valor Rejeitado: ${it.rejectedValue}"
                }
                throw new ArquivoException(message: "Erro ao salvar arquivo IntelCav (cartões sem chip).")
            }
        }
    }
}