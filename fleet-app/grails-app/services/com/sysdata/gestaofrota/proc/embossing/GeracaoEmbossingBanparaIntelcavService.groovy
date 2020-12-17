package com.sysdata.gestaofrota.proc.embossing

import com.sysdata.gestaofrota.*
import com.sysdata.xfiles.LineFeed
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

import javax.annotation.PostConstruct

@Transactional
class GeracaoEmbossingBanparaIntelcavService implements GeradorArquivoEmbossing {

    GrailsApplication grailsApplication
    private TDESChipher tdesChipher

    @PostConstruct
    def init() {
        tdesChipher = new TDESChipher("CBC", grailsApplication.config.projeto.cartao.embossing.cipher.combinedKey)
    }

    @Override
    String gerarNomeArquivo(LoteEmbossing loteEmbossing) {
        def fileDir = grailsApplication.config.projeto.arquivos.baseDir +
                grailsApplication.config.projeto.arquivos.intelcav.dir.embossing

        def fileName = sprintf("BANP_74_%05d_%s.txt", loteEmbossing.id, new Date().clearTime().format('yyyyMMdd'))

        println "FileDIr: $fileDir"
        println "FileName: $fileName"

        return fileDir + fileName
    }

    @Override
    def gerarArquivoLoteEmbossing(LoteEmbossing loteEmbossing) {

        File file = new File(gerarNomeArquivo(loteEmbossing))

        def specs = [
                SpecArquivoEmbossingBanparaIntelcav.regHeader,
                SpecArquivoEmbossingBanparaIntelcav.regCombustivel,
                SpecArquivoEmbossingBanparaIntelcav.regTrailer
        ]


        def rowCount = 0
        file.withFixedSizeWriter(LineFeed.WIN, specs) { wr ->

            rowCount = writeHeader(wr, loteEmbossing, rowCount)

            loteEmbossing.cartoes.each { crt ->
                rowCount = writeCartao(wr, crt, rowCount)

                crt.status = StatusCartao.EMBOSSING
                crt.save(flush: true)
            }
            writeTrailer(wr, rowCount)

        }
    }

    private int writeHeader(writer, LoteEmbossing loteEmbossing, int counter) {

        def vars = [
                        sequencial: ++counter,
                        data: new Date().clearTime(),
                        numeroPedido: loteEmbossing.id
                    ]

        writer.writeRecord("H", vars)
        return counter
    }

    private void writeTrailer(writer, int counter) {
        def vars = [
                        sequencial: ++counter,
                        totalCartoes: counter - 2
                    ]
        writer.writeRecord("T", vars)
    }

    private int writeCartao(writer, Cartao cartao, int counter) {

        Veiculo veiculo = ((cartao.portador) as PortadorMaquina).maquina

        def vars = [
                        sequencial: ++counter,
                        cartaoFormatado: cartao.numeroFormatado,
                        orgao: cartao.portador.unidade.rh.nomeFantasia.toUpperCase(),
                        placa: veiculo.placa,
                        marca: veiculo.marca.abreviacao.toUpperCase(),
                        modelo: veiculo.modelo.toUpperCase(),
                        combustivel: veiculo.tipoAbastecimento.nome.toUpperCase(),
                        trilha1: "B${cartao.numero}^${sprintf('%-26s', cartao.portador.nomeEmbossing)}^${cartao.validade.format('yyMM')}5060000000000000",
                        trilha2: "${cartao.numero}=${cartao.validade.format('yyMM')}5060000000000000",
                        pinBlock: this.tdesChipher.encrypt(cartao.senha)
                    ]

        writer.writeRecord("D", vars)
        return counter
    }
}
