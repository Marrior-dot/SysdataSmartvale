package com.sysdata.gestaofrota.proc.faturamento.boleto

import com.sysdata.gestaofrota.Arquivo
import com.sysdata.gestaofrota.Boleto
import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.StatusArquivo
import com.sysdata.gestaofrota.TipoArquivo
import com.sysdata.gestaofrota.TipoFatura
import com.sysdata.gestaofrota.Util
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import org.jrimum.bopepo.BancosSuportados
import org.jrimum.bopepo.view.BoletoViewer
import org.jrimum.domkee.comum.pessoa.endereco.CEP
import org.jrimum.domkee.comum.pessoa.endereco.Endereco as EnderecoBopepo
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa
import org.jrimum.domkee.financeiro.banco.febraban.Agencia
import org.jrimum.domkee.financeiro.banco.febraban.Carteira
import org.jrimum.domkee.financeiro.banco.febraban.Cedente
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta
import org.jrimum.domkee.financeiro.banco.febraban.Sacado
import org.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo
import org.jrimum.domkee.financeiro.banco.febraban.Titulo
import org.jrimum.bopepo.Boleto as BoletoBopepo


@Transactional
class BopepoGeradorBoletoBancoBrasilService implements GeradorBoleto {

    GrailsApplication grailsApplication

    @Override
    void gerarBoleto(Boleto boleto) {

        log.info "Gerando boleto da fatura ${boleto.fatura.id} ..."

        def boletoConfig = grailsApplication.config.projeto.faturamento.portador.boleto
        def adminConfig = grailsApplication.config.projeto.administradora

        Rh empresa = boleto.fatura.conta.participante

        Cedente cedente = new Cedente(adminConfig.nome, adminConfig.cnpj)

        Sacado sacado = new Sacado(empresa.nome, empresa.cnpj)

        // Informando o endereço do sacado.
        EnderecoBopepo enderecoSac = new EnderecoBopepo()
        enderecoSac.setUF(UnidadeFederativa.valueOfSigla(empresa.endereco.cidade.estado.uf))
        enderecoSac.setLocalidade(empresa.endereco.cidade.nome)
        enderecoSac.setCep(new CEP(empresa.endereco.cep))
        enderecoSac.setBairro(empresa.endereco.bairro)
        enderecoSac.setLogradouro(empresa.endereco.logradouro)
        enderecoSac.setNumero(empresa.endereco.numero)
        sacado.addEndereco(enderecoSac)

        /*
         * INFORMANDO OS DADOS SOBRE O TÍTULO.
         */


        def nossoNumero = (boletoConfig.carteira.numero in ["12", "15", "17"]) ?
                            sprintf("%07d%010d", boletoConfig.convenio as int, boleto.id) :
                            sprintf("%07d%010d", 0, 0)


        Titulo titulo = new Titulo(new ContaBancaria(), sacado, cedente, null)
        titulo.setNumeroDoDocumento(boleto.id.toString())
        titulo.setNossoNumero(nossoNumero)
        titulo.setValor(boleto.valor);
        titulo.setDataDoDocumento(boleto.dateCreated)
        titulo.setDataDoVencimento(boleto.dataVencimento)
        titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL)
        titulo.setAceite(Titulo.Aceite.N)
        titulo.setDesconto(BigDecimal.ZERO)
        titulo.setDeducao(BigDecimal.ZERO)
        titulo.setMora(BigDecimal.ZERO)
        titulo.setAcrecimo(BigDecimal.ZERO)
        titulo.setValorCobrado(BigDecimal.ZERO)

        // Informando dados sobre a conta bancária do título.
        ContaBancaria contaBancaria = titulo.getContaBancaria()
        contaBancaria.setBanco(BancosSuportados.BANCO_DO_BRASIL.create())
        contaBancaria.setNumeroDaConta(new NumeroDaConta(boletoConfig.convenio as int))
        contaBancaria.setCarteira(new Carteira(boletoConfig.carteira.numero as int))
        contaBancaria.setAgencia(new Agencia(boletoConfig.agencia as int, boletoConfig.dvAgencia))

        /*
         * INFORMANDO OS DADOS SOBRE O BOLETO.
         */
        BoletoBopepo boletoBopepo = new BoletoBopepo(titulo)

        boletoBopepo.setLocalPagamento("Pagável em qualquer Banco")
        boletoBopepo.addTextosExtras("txtRsEspecie", "R\$")
        boletoBopepo.addTextosExtras("txtFcEspecie", "R\$")



        boleto.linhaDigitavel = boletoBopepo.linhaDigitavel.write()
        boleto.titulo = boletoBopepo.titulo.numeroDoDocumento
        boleto.nossoNumero = boletoBopepo.titulo.nossoNumero
        boleto.save()

        /*
         * GERANDO O BOLETO BANCÁRIO.
         */
        // Instanciando um objeto "BoletoViewer", classe responsável pela
        // geração do boleto bancário.

        def templateFile = new File("boletoEnhanceTemplate.pdf")
        BoletoViewer boletoViewer = new BoletoViewer(boletoBopepo, templateFile)

        String baseDir = grailsApplication.config.projeto.arquivos.baseDir

        String boletoDir
        if (boleto.fatura.tipo == TipoFatura.CONVENIO_PREPAGO)
            boletoDir = grailsApplication.config.projeto.arquivos.boleto.dir.prepago
        else if (boleto.fatura.tipo == TipoFatura.CONVENIO_POSPAGO)
            boletoDir = grailsApplication.config.projeto.arquivos.boleto.dir.pospago

        baseDir = !baseDir.endsWith("/") ? baseDir + "/" : baseDir
        boletoDir = !boletoDir.endsWith("/") ? boletoDir + "/" : boletoDir

        def filenameBoleto = sprintf("%s%sGFROTA_%s_%07d.pdf",
                baseDir,
                boletoDir,
                Util.cnpjToRaw(empresa.cnpj),
                boleto.titulo as int)

        boletoViewer.getPdfAsFile(filenameBoleto)
        log.info "Boleto gerado com sucesso: $filenameBoleto"

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

    }
}
