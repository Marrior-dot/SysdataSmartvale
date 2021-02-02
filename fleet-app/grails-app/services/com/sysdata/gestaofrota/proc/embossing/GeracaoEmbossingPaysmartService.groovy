package com.sysdata.gestaofrota.proc.embossing

import com.sysdata.gestaofrota.*
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import grails.util.Environment
import grails.util.Holders

import javax.annotation.PostConstruct
import java.text.SimpleDateFormat

@Transactional
class GeracaoEmbossingPaysmartService implements GeradorArquivoEmbossing {

    GrailsApplication grailsApplication

    private TDESChipher tdesChipher

    @PostConstruct
    def init() {
        tdesChipher = new TDESChipher("CBC", grailsApplication.config.projeto.cartao.embossing.cipher.combinedKey)
    }

    @Override
    String gerarNomeArquivo(LoteEmbossing loteEmbossing) {

        def fileDir = grailsApplication.config.projeto.arquivos.baseDir +
                        grailsApplication.config.projeto.arquivos.paysmart.dir.saida

        def config = Holders.grailsApplication.config.projeto
        final String idCliente = config.cartao.embossing.idCliente
        final String idAplicacao = "FN_FROTAS"
        final String data = new SimpleDateFormat("ddMMyy").format(new Date())
        def fileName =  "${idCliente}_${config.administradora.bin}_${idAplicacao}_${data}.txt"

        return fileDir + fileName
    }

    @Override
    def gerarArquivoLoteEmbossing(LoteEmbossing loteEmbossing) {

        File file = new File(gerarNomeArquivo(loteEmbossing))

        StringBuilder builder = new StringBuilder()

        builder.append(getCabecalho(loteEmbossing))
        builder.append(getRegistros(loteEmbossing))
        builder.append(getTerminador(loteEmbossing))

        file.write(builder.toString())
    }


    private String getCabecalho(LoteEmbossing loteEmbossing) {

        def config = grailsApplication.config.projeto

        final String sequencial = "1".padLeft(8, '0')
        final String versao = "09"
        final String nomeEmpresa = String.format("%-16s", "SYSDATA")
        final String bin = config.administradora.bin
        final String produto = String.format("%-34s", config.cartao.embossing.produto)
        final String data = new SimpleDateFormat("yyyyMMdd").format(new Date())

        String fileSeq = loteEmbossing.id
        final String fileSequence = fileSeq.padLeft(5, '0')

        final String modo = Environment.DEVELOPMENT ? "TEST" : Environment.PRODUCTION ? 'PROD' : 'TEST'
        final String qtd = loteEmbossing.cartoes.size().toString().padLeft(8, '0')

        String fileNsa = loteEmbossing.id
        final String nsa = fileNsa.padLeft(5, '0')

        final String rfu = String.format("%58s", " ")

        return "H${sequencial}${versao}${nomeEmpresa}BIN=${bin}${produto}DATE=${data}" +
                "FILE SEQUENCE=${fileSequence}MODE=${modo}NUM RECORDS=${qtd}NSA=${nsa}${rfu}${terminadorLinha}"
    }

    private String normalizeColumn(String column, maxSize) {
        String normalized = Util.normalize(column)
        normalized = normalized.substring(0, Math.min(maxSize, normalized.length()))
        return sprintf("%-${maxSize}s", normalized)
    }

    private String getRegistros(LoteEmbossing loteEmbossing) {

        def maximoColunas = grailsApplication.config.projeto.cartao.embossing.maximoColunasLinhaEmbossing

        final String rfu = String.format("%7s", " ")
        final String rfu2 = String.format("%-6s", "*")
        final String rfu3 = String.format("%105s", " ")

        final String produto = "20"                          // conta corrent pf
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

        StringBuilder builder = new StringBuilder()
        int sequencial = 2

        def crtIds = Cartao.withCriteria {
                        projections {
                            property("id")
                        }
                        eq("loteEmbossing", loteEmbossing)
                        order("id")
                    }

        crtIds.eachWithIndex { cid, i ->
            Cartao cartao = Cartao.get(cid)
            String nome = cartao.portador.nomeEmbossing;
            nome = Util.normalize(nome.substring(0, Math.min(maximoColunas, nome.length())).toUpperCase())
            String pan = cartao.numero.substring(0, 16)
            String validade = sdfAAMM.format(cartao.validade)
            String via = cartao.via.toString().padLeft(2, "0")
            String discretionaryData = "${cartao.cvv}${titularidade}${via}".padRight(13, "0")

            String trilha1 = "B${pan}^${String.format("%-26s", nome)}^${validade}${serviceCode}${discretionaryData}"
            String trilha2 = "${pan}=${validade}${serviceCode}${discretionaryData}"

            String cpf = cartao.portador.cpfFormatado
            String cnpj = cartao.portador.cnpj
            String campoCpf = cpf.length() > 0 ? "CPF=${cpf}" : String.format("%18s", " ")
            String campoCnpj = cnpj.length() > 0 ? "CNPJ${cnpj}" : String.format("%19s", " ")

            def pinBlock = this.tdesChipher.encrypt(cartao.senha)

            builder.append("D${sequencial.toString().padLeft(8, '0')}${rfu}${produto}${agencia}${posto}${numeroConta}")

            // primeira linha de embossing (número do cartão formatado)
            builder.append("\$${cartao.numeroFormatado.padRight(20, " ")}")

            if (cartao.portador.instanceOf(PortadorFuncionario)) {

                // segunda linha de embossing (data validade cartão formato: MM/AA)
                builder.append("*${sdfMMAA.format(cartao.validade).padRight(maximoColunas, " ")}")

                // terceira linha de embossing (nome do portador)
                builder.append("*${normalizeColumn(cartao.portador)}")

                // quarta linha de embossing (agencia + posto; não se aplica)
                builder.append("*${emb4}")

            } else if (cartao.portador.instanceOf(PortadorMaquina)) {

                def empresaNome = normalizeColumn(cartao.portador.unidade.rh.nome, maximoColunas)
                def unidadeNome = normalizeColumn(cartao.portador.unidade.nome, maximoColunas)
                def portadorNome = normalizeColumn(cartao.portador.nomeEmbossing, maximoColunas)

                // 2ª Linha - Nome da Empresa Cliente
                builder.append("*${empresaNome}")
                // 3ª Linha - Nome da Unidade
                builder.append("*${unidadeNome}")
                // 4ª Linha - Nome do Portador
                builder.append("*${portadorNome}")

            }

            // campo reservado uso futuro
            builder.append("${rfu2}")
            // trilha 1
            builder.append("%${trilha1}?")
            // trilha 2
            builder.append(";${trilha2}?")
            builder.append("|${getDadosPostagem(cartao.portador.unidade.rh.endereco)}")
            builder.append("${cartao.cvv}${campoCpf}${campoCnpj}${campoCel}DtE=${dataEfetivacao}${rfu3}${titularidade}${via}")
            builder.append("${aplicacoes}${pinBlock}  #CH#")
            builder.append("${getTerminadorLinha()}")

            sequencial++
            cartao.status = StatusCartao.EMBOSSING
            cartao.loteEmbossing = loteEmbossing

            if ((i + 1) % 50 == 0) {
                Cartao.withSession {
                    it.flush()
                    it.clear()
                }
            }
        }

        return builder.toString()
    }

    private String getTerminador(LoteEmbossing loteEmbossing) {
        final String sequencial = (2 + loteEmbossing.cartoes.size()).toString().padLeft(8, '0')
        final String totalRegistros = loteEmbossing.cartoes.size().toString().padLeft(8, '0')

        return "T${sequencial}TOTAL=${totalRegistros}${terminadorLinha}"
    }

    private String getDadosPostagem(Endereco endereco) {
        String end = endereco?.logradouro ? endereco.logradouro.substring(0, Math.min(50, endereco.logradouro.length())).toUpperCase() : " "
        String num = endereco?.numero ? endereco.numero.substring(0, Math.min(10, endereco.numero.length())).toUpperCase() : " "
        String comp = endereco?.complemento ? endereco.complemento.substring(0, Math.min(38, endereco.complemento.length())).toUpperCase() : " "
        String bairro = endereco?.bairro ? endereco.bairro.substring(0, Math.min(30, endereco.bairro.length())).toUpperCase() : " "
        String cidade = endereco?.cidade?.nome ? endereco.cidade.nome.substring(0, Math.min(30, endereco.cidade.nome.length())).toUpperCase() : " "
        String uf = endereco?.cidade?.estado?.nome ? endereco.cidade.estado.nome.substring(0, Math.min(2, endereco.cidade.estado.nome.length())).toUpperCase() : " "
        String cep = endereco?.cep ? endereco.cep.replace("-", "") : ""

        "${String.format("%-50s", Util.normalize(end))}${String.format("%-10s", num)}${String.format("%-38s", Util.normalize(comp))}${String.format("%-30s", Util.normalize(bairro))}" +
                "${String.format("%-30s", Util.normalize(cidade))}${String.format("%-2s", Util.normalize(uf))}${String.format("%-8s", cep)}"
    }

    private String getTerminadorLinha(){
        "\r\n"
    }


}
