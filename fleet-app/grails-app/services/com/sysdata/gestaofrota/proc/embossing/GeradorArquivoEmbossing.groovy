package com.sysdata.gestaofrota.proc.embossing

import com.sysdata.gestaofrota.LoteEmbossing

interface GeradorArquivoEmbossing {

    String gerarNomeArquivo(LoteEmbossing loteEmbossing)
    def gerarArquivoLoteEmbossing(LoteEmbossing loteEmbossing)

}