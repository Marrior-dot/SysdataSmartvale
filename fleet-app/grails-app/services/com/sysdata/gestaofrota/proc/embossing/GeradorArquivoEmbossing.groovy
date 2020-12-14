package com.sysdata.gestaofrota.proc.embossing

import com.sysdata.gestaofrota.LoteEmbossing

interface GeradorArquivoEmbossing {

    def gerarArquivoLoteEmbossing(LoteEmbossing loteEmbossing)

}