package com.sysdata.gestaofrota.proc.faturamento.notafiscal

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.faturamento.portador.notafiscal.SpecArquivoRPSBarueri
import com.sysdata.xfiles.LineFeed
import com.sysdata.xfiles.SpecRecord
import grails.gorm.transactions.Transactional
import grails.util.Holders

@Transactional
class GeracaoArquivoRPSBarueriService implements ExecutableProcessing {

    private void writeHeader(writer, int novoNsa) {

        def vars = [:]
        vars.with {
            inscricaoContribuinte = Holders.grailsApplication.config.administradora.inscricaoMunicipal
            idRemessa = novoNsa
        }

        writer.writeRecord("0", vars)

    }

    private void writeDetalhe(writer, faturaList) {

        faturaList.each { Fatura fat ->

            Rh empresa = fat.conta.participante as Rh

            def vars = [:]

            vars.with {
                numeroRPS = sprintf("000%07d", fat.id)
                dataHoraRPS = new Date()
                situacaoRPS = "E"
                codigoServicoPrestado = Holders.grailsApplication.config.projeto.administradora.cnae
                enderecoServicoPrestado = Holders.grailsApplication.config.projeto.endereco_notafiscal.logradouro
                numeroLogradouroServicoPrestado = Holders.grailsApplication.config.projeto.endereco_notafiscal.numero
                complementoLogradouroServicoPrestado = Holders.grailsApplication.config.projeto.endereco_notafiscal.complemento
                bairroLogradouroServicoPrestado = Holders.grailsApplication.config.projeto.endereco_notafiscal.bairro
                cidadeLogradouroServicoPrestado = Holders.grailsApplication.config.projeto.endereco_notafiscal.cidade
                ufLogradouroServicoPrestado = Holders.grailsApplication.config.projeto.endereco_notafiscal.estado
                cepLogradouroServicoPrestado = Holders.grailsApplication.config.projeto.endereco_notafiscal.cep.replace("-", "")
                valorServico = (fat.valorTotal * 100) as long
                valorTotalRetencoes = 0L
                indicadorCPFCNPJ = 2
                CPFCNPJTomador = Util.cnpjToRaw(empresa.cnpj)
                razaoSocialTomador = Util.normalize(empresa.nome)
                razaoSocialTomador = Util.normalize(empresa.nome)
                enderecoTomador = Util.normalize(empresa.endereco.logradouro)
                numeroLogradouroTomador = Util.normalize(empresa.endereco.numero)
                complementoLogradouroTomador = Util.normalize(empresa.endereco.complemento)
                bairroLogradouroTomador = Util.normalize(empresa.endereco.bairro)
                cidadeLogradouroTomador = Util.normalize(empresa.endereco.cidade.nome)
                ufLogradouroTomador = empresa.endereco.cidade.estado.uf
                cepLogradouroTomador = empresa.endereco.cidade.estado.cep.replace("-", "")
                emailTomador = empresa.email
                numeroFatura = 0
                valorFatura = 0L

                def descServ = Holders.grailsApplication.config.projeto.faturamento.portador.notaFiscal.descriminacaoServicos

                

                def binding = [valor: fat.totalBruto]

                descricaoServico = ""
            }

            fat.statusEmissao = StatusEmissao.ARQUIVO_GERADO
            fat.save()
        }
    }

    private String prepareFilename(int nsa) {
        String baseDir = Holders.grailsApplication.config.projeto.arquivos.baseDir
        String cobrancaDir = Holders.grailsApplication.config.projeto.arquivos.cobranca.dir

        baseDir = !baseDir.endsWith("/") ? baseDir + "/" : baseDir
        cobrancaDir = !cobrancaDir.endsWith("/") ? cobrancaDir + "/" : cobrancaDir

        def fileName = sprintf("%sREM_%s_%07d.txt",
                baseDir + cobrancaDir,
                Util.cnpjToRaw(Holders.grailsApplication.config.projeto.administradora.cnpj),
                nsa)

        return fileName

    }

    @Override
    def execute(Date date) {

        List<Fatura> faturaList = Fatura.findAllByStatusEmissao(StatusEmissao.GERAR_ARQUIVO, [sort: 'data'])

        if (!faturaList.isEmpty()) {

            int novoNsa = Arquivo.nextNsa(TipoArquivo.NOTA_FISCAL)

            def fileName = prepareFilename(novoNsa)
            File file = new File(fileName)

            try {
                List<SpecRecord> specs = [
                                            SpecArquivoRPSBarueri.regHeader,
                                            SpecArquivoRPSBarueri.regDetalhe,
                                            SpecArquivoRPSBarueri.regTrailer
                                        ]

                file.withFixedSizeWriter(LineFeed.WIN, specs) { writer ->
                    writeHeader(writer, novoNsa)

                    writeDetalhe(writer, faturaList)
                }

            } catch(e) {
                if (file.exists()) {
                    file.delete()
                    log.info "Erro ao gerar arquivo Notas Fiscais. Arquivo apagado"
                }
                throw new RuntimeException(e)

            }

        } else
            log.warn "Não há faturas para gerar Nota Fiscal"



    }
}
