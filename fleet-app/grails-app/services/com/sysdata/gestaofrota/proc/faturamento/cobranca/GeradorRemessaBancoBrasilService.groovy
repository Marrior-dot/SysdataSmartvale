package com.sysdata.gestaofrota.proc.faturamento.cobranca

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import com.sysdata.xfiles.LineFeed
import com.sysdata.xfiles.SpecRecord
import grails.gorm.transactions.Transactional
import grails.util.Holders

@Transactional
class GeradorRemessaBancoBrasilService implements ExecutableProcessing {


    private void writeHeader(writer, int novoNsa) {

        def vars = [:]
        vars.with {
            identificacaoTipoOperacao = "REMESSA"
            agenciaBeneficiario = Holders.grailsApplication.config.projeto.faturamento.portador.boleto.agencia as int
            dvAgencia = Holders.grailsApplication.config.projeto.faturamento.portador.boleto.dvAgencia
            contaCorrenteBeneficiario = Holders.grailsApplication.config.projeto.faturamento.portador.boleto.conta as int
            dvContaCorrente = Holders.grailsApplication.config.projeto.faturamento.portador.boleto.dvConta
            nomeBeneficiario = Holders.grailsApplication.config.projeto.administradora.nome
            dataGravacao = new Date().clearTime()
            sequencialRemessa = novoNsa
            convenio = Holders.grailsApplication.config.projeto.faturamento.portador.boleto.convenio as int
        }

        writer.writeRecord("0", vars)

    }

    private int writeDetalhe(writer, List<Boleto> boletoList) {

        def counter = 1

        boletoList.each { bol ->

            Rh empresa = bol.fatura.conta.participante as Rh

            def vars = [:]
            vars.with {
                cpfCnpj = Holders.grailsApplication.config.projeto.administradora.cnpj as long
                prefixoAgencia = Holders.grailsApplication.config.projeto.faturamento.portador.boleto.agencia as int
                dvPrefixoAgencia = Holders.grailsApplication.config.projeto.faturamento.portador.boleto.dvAgencia
                contaCorrenteBeneficiario = Holders.grailsApplication.config.projeto.faturamento.portador.boleto.conta as int
                dvContaCorrente = Holders.grailsApplication.config.projeto.faturamento.portador.boleto.dvConta
                convenioBeneficiario = Holders.grailsApplication.config.projeto.faturamento.portador.boleto.convenio as int
                nossoNumero = Holders.grailsApplication.config.projeto.faturamento.portador.boleto.carteira.numero in ["12", "15", "17"] ? bol.nossoNumero as long: 0
                variacaoCarteira = Holders.grailsApplication.config.projeto.faturamento.portador.boleto.carteira.variacao as int
                carteiraCobranca = Holders.grailsApplication.config.projeto.faturamento.portador.boleto.carteira.numero as int
                seuNumeroNumeroTitulo = bol.titulo
                dataVencimento = bol.dataVencimento
                valorTitulo = (bol.valor * 100 as long)
                especieTitulo = 1
                aceiteTitulo = "N"
                dataEmissao = bol.dateCreated
                jurosDeMora = 0
                dataLimiteDesconto = 0
                valorDesconto = 0
                valorIOF = 0
                valorAbatimento = 0
                cpfCnpjPagador = Util.cnpjToRaw(empresa.cnpj) as long
                nomePagador = Util.normalize(empresa.nome)
                enderecoPagador = Util.normalize(empresa.endereco.logradouro)
                bairroPagador = Util.normalize(empresa.endereco.bairro)
                cepPagador = empresa.endereco.cep.replace("-", "")
                cidadePagador = Util.normalize(empresa.endereco.cidade.nome)
                ufPagador = empresa.endereco.cidade.estado.uf


            }
            vars.sequencialRegistro = ++counter
            writer.writeRecord("7", vars)

            bol.status = StatusBoleto.REMESSA
            bol.save()

        }

        return counter

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

        List<Boleto> boletoList = Boleto.findAllByStatus(StatusBoleto.CRIADO)

        if (!boletoList.isEmpty()) {

            int novoNsa = Arquivo.nextNsa(TipoArquivo.REMESSA_COBRANCA)

            def fileName = prepareFilename(novoNsa)
            File file = new File(fileName)

            try {
                List<SpecRecord> specs = [
                        SpecArquivoRemessaBancoBrasil.regHeader,
                        SpecArquivoRemessaBancoBrasil.regDetalhe,
                        SpecArquivoRemessaBancoBrasil.regTrailer
                ]

                file.withFixedSizeWriter(LineFeed.WIN, specs) { writer ->

                    writeHeader(writer, novoNsa)
                    def counter = writeDetalhe(writer, boletoList)

                    writer.writeRecord("9", [sequencialRegistro: ++counter])

                }

                Arquivo arquivoRemessa = new Arquivo()
                arquivoRemessa.with {
                    tipo = TipoArquivo.REMESSA_COBRANCA
                    nsa = novoNsa
                    status = StatusArquivo.GERADO
                    nome = fileName
                }

                arquivoRemessa.save(flush: true)

                log.info "Arquivo Remessa gerado com sucesso: ${fileName}"

            } catch (e) {
                if (file.exists()) {
                    file.delete()
                    log.info "Erro ao gerar arquivo remessa. Arquivo apagado"
                }
                throw new RuntimeException(e)
            }



        } else
            log.warn "N??o h?? boletos para arquivo de remessa cobran??a"


    }
}
