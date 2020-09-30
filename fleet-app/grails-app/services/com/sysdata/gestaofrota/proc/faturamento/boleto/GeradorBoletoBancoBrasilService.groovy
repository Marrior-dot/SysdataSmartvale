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
import com.sysdata.gestaofrota.Boleto
import com.sysdata.gestaofrota.Rh
import grails.core.GrailsApplication

class GeradorBoletoBancoBrasilService implements GeradorBoleto {

    GrailsApplication grailsApplication

    @Override
    void gerarBoleto(Boleto boleto) {

        def boletoConfig = grailsApplication.config.projeto.faturamento.portador.boleto
        def adminConfig = grailsApplication.config.projeto.administradora

        def diaVcto = boleto.dataVencimento[Calendar.DAY_OF_MONTH]
        def mesVcto = boleto.dataVencimento[Calendar.MONTH]
        def anoVcto = boleto.dataVencimento[Calendar.YEAR]

        Datas datas = Datas.novasDatas()
                            .comDocumento(diaVcto, mesVcto, anoVcto)
                            .comProcessamento(diaVcto, mesVcto, anoVcto)
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
                .comCarteira(boletoConfig.carteira)
                .comEndereco(enderecoBeneficiario)
                //.comNossoNumero("9000206");


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
                                        .comNumeroDoDocumento(boleto.id)
/*
                .comInstrucoes("instrucao 1", "instrucao 2", "instrucao 3", "instrucao 4", "instrucao 5")
                .comLocaisDePagamento("local 1", "local 2");
*/

        GeradorDeBoleto gerador = new GeradorDeBoleto(boletoStella);

        // Para gerar um boleto em PDF
        gerador.geraPDF("/home/luiz/frota/bahiavale/BancoDoBrasil.pdf");

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
