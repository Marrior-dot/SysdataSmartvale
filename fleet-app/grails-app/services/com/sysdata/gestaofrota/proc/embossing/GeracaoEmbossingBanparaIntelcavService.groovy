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

    private static final String CARTAO_EXTRA = "CARTAO EXTRA"

    @PostConstruct
    def init() {
        tdesChipher = new TDESChipher("CBC", grailsApplication.config.projeto.cartao.embossing.cipher.combinedKey)
    }

    @Override
    String gerarNomeArquivo(LoteEmbossing loteEmbossing) {
        def fileDir = grailsApplication.config.projeto.arquivos.baseDir +
                grailsApplication.config.projeto.arquivos.intelcav.dir.saida

        def projetoSubprojeto
        if (loteEmbossing.tipo == TipoLoteEmbossing.PADRAO)
            projetoSubprojeto = 74
        else if (loteEmbossing.tipo == TipoLoteEmbossing.PROVISORIO)
            projetoSubprojeto = 75
        else
            throw new RuntimeException("Tipo Lote Embossing não identificado!")

        def fileName = sprintf("BANP_%02d_%05d_%s.txt",
                                projetoSubprojeto,
                                loteEmbossing.id,
                                new Date().clearTime().format('yyyyMMdd'))

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

        def vars = [
                        sequencial: ++counter,
                        cartaoFormatado: cartao.numeroFormatado,
                        trilha2: "${cartao.numero}=${cartao.validade.format('yyMM')}5060000000000000",
                        pinBlock: this.tdesChipher.encrypt(cartao.senha)
                    ]

        if (cartao.tipo == TipoCartao.PROVISORIO) {
            vars += [
                orgao: CARTAO_EXTRA,
                placa: "",
                marca: "",
                modelo: "",
                combustivel: "",
                trilha1: "B${cartao.numero}^${sprintf('%-26s', "")}^${cartao.validade.format('yyMM')}5060000000000000",
            ]

        } else {
            MaquinaMotorizada maquinaMotorizada = ((cartao.portador) as PortadorMaquina).maquina
            vars += [
                    orgao: Util.normalize(cartao.portador.unidade.rh.nomeFantasia.toUpperCase()),

                    placa: maquinaMotorizada.instanceOf(Veiculo) ? (maquinaMotorizada as Veiculo).placa :
                            (maquinaMotorizada as Equipamento).codigo,

                    marca: Util.normalize(maquinaMotorizada.instanceOf(Veiculo) ? (maquinaMotorizada as Veiculo).marca.abreviacao.toUpperCase() :
                            (maquinaMotorizada as Equipamento).tipo.abreviacao.toUpperCase()),

                    modelo: Util.normalize(maquinaMotorizada.instanceOf(Veiculo) ? (maquinaMotorizada as Veiculo).modelo.toUpperCase() :
                            (maquinaMotorizada as Equipamento).complementoEmbossing),

                    combustivel: maquinaMotorizada.tipoAbastecimento.nome.toUpperCase(),
                    trilha1: "B${cartao.numero}^${sprintf('%-26s', Util.normalize(cartao.portador.nomeEmbossing.length() > 26 ? cartao.portador.nomeEmbossing[0..25] : cartao.portador.nomeEmbossing))}^${cartao.validade.format('yyMM')}5060000000000000"
            ]

        }
        writer.writeRecord("D", vars)
        return counter
    }
}
