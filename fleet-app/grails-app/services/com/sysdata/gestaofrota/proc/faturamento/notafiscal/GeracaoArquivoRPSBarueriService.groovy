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

    private void writeHeader(writer, int novoNsa, Map vars) {

        vars.with {
            inscricaoContribuinte = Holders.grailsApplication.config.projeto.administradora.inscricaoMunicipal
            idRemessa = novoNsa
        }

        writer.writeRecord("1", vars)
        vars.numeroTotalLinhasArquivo = 1
        vars.valorTotalServicos = 0

    }

    private String writeDescServicos(Fatura fat) {
        def taxas = []
        Lancamento taxaAdm = fat.itens.find { it.lancamento.tipo == TipoLancamento.TAXA_ADM }
        if (taxaAdm)
            taxas << taxaAdm

        Lancamento taxaDesc = fat.itens.find { it.lancamento.tipo == TipoLancamento.TAXA_DESCONTO }
        if (taxaDesc)
            taxas << taxaDesc

        def descServ = Holders.grailsApplication.config.projeto.faturamento.portador.notaFiscal.descriminacaoServicos

        def binding = [
                        valorTotal: fat.valorTotal,
                        valorConsumido: fat.itens.sum { it.lancamento.tipo == TipoLancamento.CARGA ? it.valor : 0 },
                        taxas: taxas.size() > 0 ? taxas.sum { it.tipo.nome + "	" + it.valor + '|' } : ''
                    ]

        def engine = new groovy.text.SimpleTemplateEngine()
        def template = engine.createTemplate(descServ).make(binding)

        return template.toString()
    }


    private void writeDetalhe(writer, List<Fatura> faturaList, Map vars) {

        faturaList.each { Fatura fat ->

            Rh empresa = fat.conta.participante as Rh

            vars.with {
                numeroRPS = sprintf("000%07d", fat.id)
                dataHoraRPS = new Date()
                situacaoRPS = "E"
                codigoServicoPrestado = Holders.grailsApplication.config.projeto.administradora.cnae as int
                enderecoServicoPrestado = Util.normalize(Holders.grailsApplication.config.projeto.administradora.enderecoNotaFiscal.logradouro)
                numeroLogradouroServicoPrestado = Util.normalize(Holders.grailsApplication.config.projeto.administradora.enderecoNotaFiscal.numero)
                complementoLogradouroServicoPrestado = Util.normalize(Holders.grailsApplication.config.projeto.administradora.enderecoNotaFiscal.complemento)
                bairroLogradouroServicoPrestado = Util.normalize(Holders.grailsApplication.config.projeto.administradora.enderecoNotaFiscal.bairro)
                cidadeLogradouroServicoPrestado = Util.normalize(Holders.grailsApplication.config.projeto.administradora.enderecoNotaFiscal.cidade)
                ufLogradouroServicoPrestado = Util.normalize(Holders.grailsApplication.config.projeto.administradora.enderecoNotaFiscal.estado)
                cepLogradouroServicoPrestado = Holders.grailsApplication.config.projeto.administradora.enderecoNotaFiscal.cep.replace("-", "")
                valorServico = (fat.valorTotal * 100) as long
                valorTotalRetencoes = 0L
                indicadorCPFCNPJ = 2
                CPFCNPJTomador = Util.cnpjToRaw(empresa.cnpj) as long
                razaoSocialTomador = Util.normalize(empresa.nome)
                enderecoTomador = Util.normalize(empresa.endereco.logradouro)
                numeroLogradouroTomador = Util.normalize(empresa.endereco.numero)
                complementoLogradouroTomador = Util.normalize(empresa.endereco.complemento)
                bairroLogradouroTomador = Util.normalize(empresa.endereco.bairro)
                cidadeLogradouroTomador = Util.normalize(empresa.endereco.cidade.nome)
                ufLogradouroTomador = empresa.endereco.cidade.estado.uf
                cepLogradouroTomador = empresa.endereco.cep.replace("-", "")
                emailTomador = empresa.email
                numeroFatura = 0
                valorFatura = 0L
                descricaoServico = writeDescServicos(fat)

            }

            fat.statusEmissao = StatusEmissao.ARQUIVO_GERADO
            fat.save()

            writer.writeRecord("2", vars)

            vars.numeroTotalLinhasArquivo++
            vars.valorTotalServicos += vars.valorServico
        }
    }

    private void writeTrailer(writer, vars) {

        vars.numeroTotalLinhasArquivo++
        vars.valorTotalRetencao = 0

        writer.writeRecord("9", vars)

    }

    private String prepareFilename(int nsa) {
        String baseDir = Holders.grailsApplication.config.projeto.arquivos.baseDir
        String nfeDir = Holders.grailsApplication.config.projeto.arquivos.notaFiscal.dir

        baseDir = !baseDir.endsWith("/") ? baseDir + "/" : baseDir
        nfeDir = !nfeDir.endsWith("/") ? nfeDir + "/" : nfeDir

        def fileName = sprintf("%sRPS_%s_%07d.txt",
                                    baseDir + nfeDir,
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

                    def vars = [:]
                    writeHeader(writer, novoNsa, vars)
                    writeDetalhe(writer, faturaList, vars)
                    writeTrailer(writer, vars)
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
