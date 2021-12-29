package com.sysdata.gestaofrota

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.exception.ArquivoException
import com.sysdata.gestaofrota.proc.embossing.GeradorArquivoEmbossing
import com.sysdata.gestaofrota.processamento.embossadoras.Embossadora
import com.sysdata.gestaofrota.processamento.embossadoras.IntelCav
import com.sysdata.gestaofrota.processamento.embossadoras.PaySmart
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional


class GeracaoArquivoEmbossingService implements ExecutableProcessing {

    GrailsApplication grailsApplication

    private void gerarArquivoEmFileSystem(Arquivo arquivo) {

        def fileDir = grailsApplication.config.projeto.arquivos.baseDir +
                        grailsApplication.config.projeto.arquivos.paysmart.dir.saida

        File file = new File("${fileDir}/${arquivo.nome}")
        file.withWriter { w ->
            w.write(arquivo.conteudoText)
        }
        log.info "Arquivo Gerado em ${file.absoluteFile}"

    }

    @Transactional
    def regerarArquivo(Arquivo arquivo) {
        log.info "Iniciando Regeração do Arquivo Embossing #$arquivo.id ..."

        def crtIdsList = Cartao.withCriteria {
            projections {
                property "id"
            }
            eq("arquivo", arquivo)
            eq("status", StatusCartao.EMBOSSING)
        }
        def ret

        if (crtIdsList) {
            Cartao cartao = Cartao.get(crtIdsList[0])
            if (cartao.portador.unidade.rh.cartaoComChip) {
                log.info "Qtde Cartões Com Chip: ${crtIdsList.size()}"
                Embossadora embossadora = new PaySmart(crtIdsList)
                ret = embossadora.regerar(arquivo)
                if (ret.success)
                    gerarArquivoEmFileSystem(arquivo)
            } else {
                log.info "Qtde Cartões Sem Chip: ${crtIdsList.size()}"
                Embossadora embossadora = new IntelCav(crtIdsList)
                ret = embossadora.regerar(arquivo)
            }
        } else {
            ret = [success: false, message: "Arquivo não pode ser regerado. Cartões não mais em embossing."]
        }
        log.info "Regeração finalizada!"
        ret
    }

    @Transactional
    boolean gerarArquivo() {

        log.info "Iniciando Geração Embossing..."
        boolean cartaoComChip = true
        Closure criteria = {
            projections {
                property "id"
            }
            eq('status', StatusCartao.CRIADO)
            portador {
                unidade {
                    rh {
                        eq('cartaoComChip', cartaoComChip)
                    }
                }
            }
        }

        List<Long> cartoesComChip = Cartao.createCriteria().list(criteria);
        cartaoComChip = false
        List<Long> cartoesSemChip = Cartao.createCriteria().list(criteria)
        Embossadora embossadora = null

      if (cartoesComChip.size() > 0) {
            log.info "Qtde com chip: ${cartoesComChip.size()}"
            embossadora = new PaySmart(cartoesComChip)
            Arquivo arquivo = embossadora.gerar()

            if (arquivo.save(flush: true)) {

                gerarArquivoEmFileSystem(arquivo)
                return true

            } else {
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

    @Transactional
    def marcarEnviado(file) {
        Arquivo arqEmbossing = Arquivo.withCriteria(uniqueResult: true) {
                                    like("nome", "%" + file.fileName)
                                    eq("status", StatusArquivo.GERADO)
                                }
        if (arqEmbossing) {

            LoteEmbossing loteEmbossing = LoteEmbossing.withCriteria(uniqueResult: true) {
                                                arquivos {
                                                    eq("id", arqEmbossing.id)
                                                }
                                            }
            if (loteEmbossing) {
                loteEmbossing.status = StatusLoteEmbossing.ENVIADO_EMBOSSADORA
                loteEmbossing.save(flush: true)
                log.info "Lote Embossing #${loteEmbossing.id} marcado como ${StatusLoteEmbossing.ENVIADO_EMBOSSADORA}"

                arqEmbossing.status = StatusArquivo.ENVIADO
                arqEmbossing.save(flush: true)
                log.info "Arquivo #$arqEmbossing.id marcado como ${StatusArquivo.ENVIADO}"

            } else
                log.error "Nenhum Lote Embossing foi encontrado vinculado ao arquivo (${file.fileName})"

        } else
            log.error "Arquivo ($file.fileName) não encontrado na base"

    }

    @Override
    def execute(Date date) {
        def loteEmbossingList = LoteEmbossing.where { status == StatusLoteEmbossing.CRIADO }
                                            .list(sort: "dateCreated")

        def gerador = grailsApplication.config.projeto.cartao.embossing.gerador

        if (!gerador || !grailsApplication.mainContext.containsBean(gerador))
            throw new RuntimeException("Gerador arquivo embossing inexistente: ${gerador}")

        GeradorArquivoEmbossing geradorArquivoEmbossing = grailsApplication.mainContext.getBean(gerador)

        if (loteEmbossingList) {

            loteEmbossingList*.id.each { loteId ->

                LoteEmbossing lote = LoteEmbossing.get(loteId)

                def fileName = geradorArquivoEmbossing.gerarNomeArquivo(lote)

                Arquivo arquivoEmbossing = new Arquivo(
                        nome: fileName,
                        tipo: TipoArquivo.EMBOSSING,
                        status: StatusArquivo.PROCESSANDO,
                        nsa: Arquivo.nextNsa(TipoArquivo.EMBOSSING))
                arquivoEmbossing.save(flush: true)

                lote.arquivos << arquivoEmbossing

                geradorArquivoEmbossing.gerarArquivoLoteEmbossing(lote)
                if (! lote.isAttached())
                    lote = LoteEmbossing.get(loteId)

                arquivoEmbossing.status = StatusArquivo.GERADO
                arquivoEmbossing.save(flush: true)
                lote.status = StatusLoteEmbossing.ARQUIVO_GERADO
                lote.save(flush: true)
                log.info "Lote Embossing #${lote.id} processado"
            }
        } else
            log.info "Não há Lotes Embossing para gerar arquivo"

    }
}