package com.sysdata.gestaofrota.processamento.embossadoras

import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.Arquivo
import com.sysdata.gestaofrota.Cartao
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.StatusArquivo
import com.sysdata.gestaofrota.TipoArquivo
import com.sysdata.gestaofrota.processamento.IGeradorArquivo


/**
 * Created by hyago on 05/10/17.
 */
abstract class Embossadora implements IGeradorArquivo {
    protected List<Cartao> cartoes
    private String m_Bin

    String getBin() {
        return m_Bin
    }

    Embossadora(List<Cartao> cartoes) {
        this.cartoes = cartoes
        this.m_Bin = Administradora.list().first().bin
    }

    @Override
    Arquivo gerar() {
        Arquivo arquivoInstance = new Arquivo(
                nome: getNomeArquivo(),
                tipo: TipoArquivo.EMBOSSING,
                status: StatusArquivo.PROCESSANDO )

        StringBuilder builder = new StringBuilder()

        builder.append(getCabecalho())
        builder.append(getRegistros(arquivoInstance))
        builder.append(getTerminador())

        arquivoInstance.conteudoText = builder.toString()
        arquivoInstance.status = StatusArquivo.GERADO

        return arquivoInstance
    }

    protected String getNomeTitular(Portador portador) {
        portador.nomeEmbossing.substring(0,
                Math.min(portador.nomeEmbossing.length(), getTamanhoMaximoNomeTitular())
        ).toUpperCase()
    }
    protected String getNumeroCartaoFormatado(String numero) {
        numero = numero.replaceAll(" ", "")
        "${numero.subSequence(0, 4)} ${numero.subSequence(4, 8)} ${numero.subSequence(8, 12)} ${numero.subSequence(12, 16)}"
    }
    protected String getTerminadorLinha(){
        "\r\n"
    }

    protected abstract int getTamanhoMaximoNomeTitular()
    protected abstract String getNomeArquivo()
    protected abstract String getCabecalho(Arquivo arquivo) //Header
    protected abstract String getRegistros(Arquivo arquivo) //Records
    protected abstract String getTerminador() //Trailer
}