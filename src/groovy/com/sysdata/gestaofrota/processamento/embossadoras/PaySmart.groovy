package com.sysdata.gestaofrota.processamento.embossadoras

import com.sysdata.gestaofrota.Arquivo
import com.sysdata.gestaofrota.Cartao
import com.sysdata.gestaofrota.Endereco
import com.sysdata.gestaofrota.PortadorFuncionario
import com.sysdata.gestaofrota.Telefone

import java.text.SimpleDateFormat

/**
 * Created by hyago on 04/10/17.
 */
class PaySmart extends Embossadora {

    PaySmart(List<Cartao> cartoes) {
        super(cartoes)
    }

    @Override
    protected int getTamanhoMaximoNomeTitular() {
        return 24
    }

    @Override
    protected String getNomeArquivo() {
        final String idCliente = "MAXXC"
        final String idAplicacao = "VN"
        final String data = new SimpleDateFormat("ddMMyy").format(new Date())
        final String idPerfilEletronico = "01"
        final String idAgrupamentoPostagem = "0"    //TODO perguntar
        final String idQuebraDepartamento = "0"     //TODO perguntar
        final String flagImpressaoSenha = "1"       //TODO perguntar

        "${idCliente}_${getBin()}_${idAplicacao}_${data}_${idPerfilEletronico}${idAgrupamentoPostagem}${idQuebraDepartamento}${flagImpressaoSenha}"
    }

    @Override
    protected String getCabecalho() {
        final String sequencial = "1".padLeft(8, '0')
        final String versao = "07"
        final String nomeEmpresa = String.format("%-16s", "ACCEPTION")
        final String bin = "000000"
        final String produto = String.format("%-34s", "ProdutoTeste")
        final String data = new SimpleDateFormat("yyyyMMdd").format(new Date())
        final String fileSequence = "1".padLeft(5, '0')
        final String modo = "TEST" //TODO: mudar para 'PROD'
        final String qtd = cartoes.size().toString().padLeft(8, '0')
        final String nsa = fileSequence
        final String rfu = String.format("%58s", " ")

        return "H${sequencial}${versao}${nomeEmpresa}BIN=${bin}${produto}DATE=${data}" +
                "FILE SEQUENCE=${fileSequence}MODE=${modo}NUM RECORDS=${qtd}NSA=${nsa}${rfu}${terminadorLinha}"
    }

    @Override
    protected String getRegistros(Arquivo arquivo) {
        final String rfu = String.format("%7s", " ")
        final String rfu2 = String.format("%-6s", "*")
        final String rfu3 = String.format("%105s", " ")
        final String tipoCartao = "20"                          // conta corrent pf
        final String agencia = String.format("%4s", " ")        // não se aplica
        final String posto = String.format("%2s", " ")          // não se aplica
        final String numeroConta = String.format("%10s", " ")   // não se aplica
        final SimpleDateFormat sdfMMAA = new SimpleDateFormat("MM/YY")
        final SimpleDateFormat sdfAAMM = new SimpleDateFormat("YYMM")
        final String emb4 = String.format("%-24s", " ")
        final String serviceCode = "606"
        final String titularidade = "01"
        final String campoCel = String.format("%19s", " ")
        final String dataEfetivacao = new SimpleDateFormat("yyMMdd").format(new Date())
        final String aplicacoes = String.format("%10s", " ")
        final String binBlock = String.format("%16s", " ")


        StringBuilder builder = new StringBuilder()
        int sequencial = 2
        cartoes.each { c ->
            String nome = c.portador.nomeEmbossing; nome = nome.substring(0, Math.min(26, nome.length())).toUpperCase()
            String pan = c.numero.substring(0, 16)
            String validade = sdfAAMM.format(c.validade)
            String via = c.via.toString().padLeft(2, "0")
            String discretionaryData = "${c.cvv}${titularidade}${via}".padRight(13, "0")

            String trilha1 = "B${pan}^${String.format("%-26s", nome)}^${validade}${serviceCode}${discretionaryData}"
            String trilha2 = "${pan}=${validade}${serviceCode}${discretionaryData}"

            String cpf = c.portador.cpfFormatado
            String cnpj = c.portador.cnpj
            String campoCpf = cpf.length() > 0 ? "CPF=${cpf}" : String.format("%18s", " ")
            String campoCnpj = cnpj.length() > 0 ? "CNPJ${cnpj}" : String.format("%19s", " ")

            builder.append("D${sequencial.toString().padLeft(8, '0')}${rfu}${tipoCartao}${agencia}${posto}${numeroConta}" +
                    "\$${getNumeroCartaoFormatado(c.numero)} " +            // primeira linha de embossing (número do cartão formatado)
                    "*${sdfMMAA.format(c.validade).padRight(24, " ")}" +    // segunda linha de embossing (data validade cartão formato: MM/AA)
                    "*${getNomeTitular(c.portador).padRight(24, " ")}" +    // terceira linha de embossing (nome do portador)
                    "*${emb4}" +                                            // quarta linha de embossing (agencia + posto; não se aplica)
                    "${rfu2}" +                                             // campo reservado uso futuro
                    "%${trilha1}?" +                                        // trilha 1
                    ";${trilha2}?" +                                        // trilha 2
                    "|${getDadosPostagem(c.portador.endereco)}" +
                    "${c.cvv}${campoCpf}${campoCnpj}${campoCel}DtE=${dataEfetivacao}${rfu3}${titularidade}${via}" +
                    "${aplicacoes}${binBlock}  #CH#" +
                    "${getTerminadorLinha()}")

            sequencial++
        }

        return builder.toString()
    }

    @Override
    protected String getTerminador() {
        final String sequencial = (2 + cartoes.size()).toString().padLeft(8, '0')
        final String totalRegistros = cartoes.size().toString().padLeft(8, '0')

        return "T${sequencial}TOTAL=${totalRegistros}${terminadorLinha}"
    }

    private String getDadosPostagem(Endereco endereco) {
        String end = endereco?.logradouro ? endereco.logradouro.substring(0, Math.min(50, endereco.logradouro.length())).toUpperCase() : " "
        String num = endereco?.numero ? endereco.numero.substring(0, Math.min(10, endereco.numero.length())).toUpperCase() : " "
        String comp = endereco?.complemento ? endereco.complemento.substring(0, Math.min(38, endereco.complemento.length())).toUpperCase() : " "
        String bairro = endereco?.bairro ? endereco.bairro.substring(0, Math.min(30, endereco.bairro.length())).toUpperCase() : " "
        String cidade = endereco?.cidade?.nome ? endereco.cidade.nome.substring(0, Math.min(30, endereco.cidade.nome.length())).toUpperCase() : " "
        String uf = endereco?.cidade?.estado?.nome ? endereco.cidade.estado.nome.substring(0, Math.min(2, endereco.cidade.estado.nome.length())).toUpperCase() : " "
        String cep = endereco?.cep ? endereco.cep.substring(0, Math.min(8, endereco.cep.length())).toUpperCase().replace('-', '') : " "

        "${String.format("%-50s", end)}${String.format("%-10s", num)}${String.format("%-38s", comp)}${String.format("%-30s", bairro)}" +
                "${String.format("%-30s", cidade)}${String.format("%-2s", uf)}${String.format("%-8s", cep)}"
    }
}