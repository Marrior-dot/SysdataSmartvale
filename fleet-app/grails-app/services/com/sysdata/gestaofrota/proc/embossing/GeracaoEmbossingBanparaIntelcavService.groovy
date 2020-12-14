package com.sysdata.gestaofrota.proc.embossing

import com.sysdata.gestaofrota.Cartao
import com.sysdata.gestaofrota.LoteEmbossing
import com.sysdata.xfiles.LineFeed
import grails.gorm.transactions.Transactional

@Transactional
class GeracaoEmbossingBanparaIntelcavService implements GeradorArquivoEmbossing {

    @Override
    def gerarArquivoLoteEmbossing(LoteEmbossing loteEmbossing) {
        def specs = [
                        SpecArquivoEmbossingBanparaIntelcav.regHeader,
                        SpecArquivoEmbossingBanparaIntelcav.regCombustivel
                    ]

        File file = new File()

        file.withFixedSizeWriter(LineFeed.WIN, specs) { wr ->

            writeHeader(wr)

            loteEmbossing.cartoes { crt ->
                writeCartao(wr, crt)
            }

        }
    }

    private void writeHeader(writer, LoteEmbossing loteEmbossing) {

        def vars = [
                        data: new Date().clearTime(),
                        numeroPedido: loteEmbossing.id
                    ]

        writer.writeRecord("H", vars)
    }

    private void writeCartao(writer, Cartao cartao) {
        def vars = [
                data: new Date().clearTime(),
                numeroPedido: loteEmbossing.id
        ]

        writer.writeRecord("D", vars)

    }
}
