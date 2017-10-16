package com.sysdata.gestaofrota.processamento.embossadoras

import com.sysdata.gestaofrota.Arquivo
import com.sysdata.gestaofrota.Cartao
import java.text.SimpleDateFormat

/**
 * Created by hyago on 05/10/17.
 */
class IntelCav extends Embossadora {

    IntelCav(List<Cartao> cartoes) {
        super(cartoes)
    }

    @Override
    protected int getTamanhoMaximoNomeTitular() {
        return 27
    }

    @Override
    protected String getNomeArquivo() {
        return String.format("ARQ_EMB_%s.emb", new SimpleDateFormat("yyyyMMddHHmm").format(new Date()))
    }

    @Override
    protected String getCabecalho() {
        return ""
    }

    @Override
    protected String getRegistros(Arquivo arquivo) {
        StringBuilder builder = new StringBuilder()
        SimpleDateFormat anoFormatter = new SimpleDateFormat("yy")
        SimpleDateFormat mesFormatter = new SimpleDateFormat("MM")

        cartoes.each { c ->

            builder.append(String.format("""#DCC##EMB#%4s %4s %4s %3s"%-27s"%-27s"#ENC#;%19s=%2s%2s000001060000%1s?#END#@@@@@@""",
                    c.numero.substring(4, 8),
                    c.numero.substring(8, 12),
                    c.numero.substring(12, 16),
                    c.numero.substring(16, 19),
                    getNomeTitular(c.portador),
                    c.portador.unidade.rh.nome.size() > 27 ? c.portador.unidade.rh.nome.substring(0, 27) : c.portador.unidade.rh.nome,
                    c.numero,
                    anoFormatter.format(c.validade),
                    mesFormatter.format(c.validade),
                    c.numero.substring(18, 19)
            ))
            builder.append(terminadorLinha)
        }

        return builder.toString()
    }

    @Override
    protected String getTerminador() {
        return ""
    }
}