package com.sysdata.gestaofrota.proc.faturamento.boleto

import br.com.caelum.stella.boleto.Banco as BancoStella
import br.com.caelum.stella.boleto.Beneficiario
import br.com.caelum.stella.boleto.Boleto as BoletoStella
import br.com.caelum.stella.boleto.Datas
import br.com.caelum.stella.boleto.Endereco as EnderecoStella
import br.com.caelum.stella.boleto.Pagador
import br.com.caelum.stella.boleto.bancos.BancoDoBrasil
import br.com.caelum.stella.boleto.transformer.GeradorDeBoleto
import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.Arquivo
import com.sysdata.gestaofrota.Boleto
import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.StatusArquivo
import com.sysdata.gestaofrota.TipoArquivo
import com.sysdata.gestaofrota.TipoFatura
import com.sysdata.gestaofrota.Util
import grails.core.GrailsApplication

class GeradorBoletoBancoBrasilService implements GeradorBoleto {

    GrailsApplication grailsApplication


    @Override
    void gerarBoleto(Boleto boleto) {

        log.info "Gerando boleto da fatura ${boleto.fatura.id} ..."

        def boletoConfig = grailsApplication.config.projeto.faturamento.portador.boleto
        def adminConfig = grailsApplication.config.projeto.administradora

        def diaVcto = boleto.dataVencimento[Calendar.DAY_OF_MONTH]
        def mesVcto = boleto.dataVencimento[Calendar.MONTH]
        def anoVcto = boleto.dataVencimento[Calendar.YEAR]

        Datas datas = Datas.novasDatas()
                            .comVencimento(diaVcto, mesVcto, anoVcto)

        Administradora admin = Administradora.first()

        EnderecoStella enderecoBeneficiario = EnderecoStella.novoEndereco()
                                                .comLogradouro(adminConfig.endereco.logradouro + ", " + adminConfig.endereco.numero)
                                                .comBairro(adminConfig.endereco.bairro)
                                                .comCep(adminConfig.endereco.cep)
                                                .comCidade(adminConfig.endereco.cidade)
                                                .comUf(adminConfig.endereco.estado)

        //Quem emite o boleto
        Beneficiario beneficiario = Beneficiario.novoBeneficiario()
                .comNomeBeneficiario(admin.nome)
                .comAgencia(boletoConfig.agencia)
                .comDigitoAgencia(boletoConfig.dvAgencia)
                .comCodigoBeneficiario(boletoConfig.conta)
                .comDigitoCodigoBeneficiario(boletoConfig.dvConta)
                .comNumeroConvenio(boletoConfig.convenio)
                .comCarteira(boletoConfig.carteira.numero)
                .comEndereco(enderecoBeneficiario)
                .comNossoNumero(boletoConfig.convenio)


        Rh empresa = boleto.fatura.conta.participante

        EnderecoStella enderecoPagador = EnderecoStella.novoEndereco()
                                            .comLogradouro(empresa.endereco.logradouro)
                                            .comBairro(empresa.endereco.bairro)
                                            .comCep(empresa.endereco.cep)
                                            .comCidade(empresa.endereco.cidade.nome)
                                            .comUf(empresa.endereco.cidade.estado.uf);

        //Quem paga o boleto
        Pagador pagador = Pagador.novoPagador()
                            .comNome(empresa.nome)
                            .comDocumento(empresa.cnpj)
                            .comEndereco(enderecoPagador);

        BancoStella banco = new BancoDoBrasil();

        BoletoStella boletoStella = BoletoStella.novoBoleto()
                                        .comBanco(banco)
                                        .comDatas(datas)
                                        .comBeneficiario(beneficiario)
                                        .comPagador(pagador)
                                        .comValorBoleto(boleto.valor)
                                        .comNumeroDoDocumento(boleto.id.toString())
                                        .comLocaisDePagamento("Pagável em qualquer banco até a data de vencimento")
/*
                .comInstrucoes("instrucao 1", "instrucao 2", "instrucao 3", "instrucao 4", "instrucao 5")
                .comLocaisDePagamento("local 1", "local 2");
*/

        boleto.linhaDigitavel = boletoStella.linhaDigitavel
        boleto.titulo = boletoStella.numeroDoDocumento
        boleto.nossoNumero = boletoStella.nossoNumeroECodDocumento
        boleto.save()

        println "Nosso Num & Cod Doc: ${boletoStella.nossoNumeroECodDocumento}"
        println "Num Doc: ${boletoStella.numeroDoDocumento}"
        println "Num Doc Formatado: ${boletoStella.numeroDoDocumentoFormatado}"

        GeradorDeBoleto gerador = new GeradorDeBoleto(boletoStella)

        String baseDir = grailsApplication.config.projeto.arquivos.baseDir

        String boletoDir
        if (boleto.fatura.tipo == TipoFatura.CONVENIO_PREPAGO)
            boletoDir = grailsApplication.config.projeto.arquivos.boleto.dir.prepago
        else if (boleto.fatura.tipo == TipoFatura.CONVENIO_POSPAGO)
            boletoDir = grailsApplication.config.projeto.arquivos.boleto.dir.pospago

        baseDir = !baseDir.endsWith("/") ? baseDir + "/" : baseDir
        boletoDir = !boletoDir.endsWith("/") ? boletoDir + "/" : boletoDir

        def filenameBoleto = sprintf("%s%s%s_%s.pdf",
                                                baseDir,
                                                boletoDir,
                                                Util.cnpjToRaw(empresa.cnpj),
                                                boleto.dateCreated.format('yyMMdd'))

        // Para gerar um boleto em PDF
        gerador.geraPDF(filenameBoleto)

        Arquivo arquivoBoleto = new Arquivo()
        arquivoBoleto.with {
            nome = filenameBoleto
            tipo = TipoArquivo.BOLETO
            status = StatusArquivo.GERADO
            nsa = Arquivo.nextNsa(TipoArquivo.BOLETO)
        }
        arquivoBoleto.save()

        boleto.arquivo = arquivoBoleto
        boleto.save(flush: true)

        log.info "Boleto gerado com sucesso: $filenameBoleto"

/*
        // Para gerar um boleto em PNG
        gerador.geraPNG("BancoDoBrasil.png");

        // Para gerar um array de bytes a partir de um PDF
        byte[] bPDF = gerador.geraPDF();

        // Para gerar um array de bytes a partir de um PNG
        byte[] bPNG = gerador.geraPNG();
*/
    }

}
