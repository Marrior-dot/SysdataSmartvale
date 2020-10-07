package com.sysdata.gestaofrota.processamento.embossadoras

import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.Arquivo
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.StatusArquivo
import com.sysdata.gestaofrota.TipoArquivo
import com.sysdata.gestaofrota.processamento.IGeradorArquivo
import groovy.util.logging.Slf4j


/**
 * Created by hyago on 05/10/17.
 */

@Slf4j
abstract class Embossadora implements IGeradorArquivo {
    protected List<Long> cartoesIds
    private String m_Bin

    String getBin() {
        return m_Bin
    }

    Embossadora(List<Long> cartoesIds) {
        this.cartoesIds = cartoesIds
        this.m_Bin = Administradora.list().first().bin
    }

    @Override
    def regerar(Arquivo arquivoOriginal) {
        def ret = [success: true]
        if (arquivoOriginal.status == StatusArquivo.REGERADO) {
            ret.success = false
            ret.message = "Operação inválida! Arquivo #$arquivoOriginal.id já regerado anteriormente"
            return ret
        } else if (arquivoOriginal.status != StatusArquivo.GERADO) {
            ret.success = false
            ret.message = "Operação inválida! Arquivo #$arquivoOriginal.id com status $arquivoOriginal.status.name não pode ser Regerado"
            return ret
        }

        arquivoOriginal.status = StatusArquivo.REGERADO
        arquivoOriginal.save()

        Arquivo novoArquivo = new Arquivo(
                nome: getNomeArquivo(),
                tipo: TipoArquivo.EMBOSSING,
                status: StatusArquivo.PROCESSANDO,
                lote: arquivoOriginal.lote,
                nsa: Arquivo.nextNsa(TipoArquivo.EMBOSSING))

        StringBuilder builder = new StringBuilder()

        builder.append(getCabecalho(novoArquivo))
        builder.append(getRegistros(novoArquivo))
        builder.append(getTerminador())

        novoArquivo.conteudoText = builder.toString()
        novoArquivo.status = StatusArquivo.GERADO

        novoArquivo.save(flush: true)

        ret.message = "Arquivo Regerado"
        ret
    }


    @Override
    Arquivo gerar() {
        Arquivo arquivoInstance = new Arquivo(
                nome: getNomeArquivo(),
                tipo: TipoArquivo.EMBOSSING,
                status: StatusArquivo.PROCESSANDO,
                lote: Arquivo.nextLote(TipoArquivo.EMBOSSING),
                nsa: Arquivo.nextNsa(TipoArquivo.EMBOSSING))

        StringBuilder builder = new StringBuilder()

        builder.append(getCabecalho(arquivoInstance))
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