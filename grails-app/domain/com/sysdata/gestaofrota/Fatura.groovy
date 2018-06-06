package com.sysdata.gestaofrota

import com.mrkonno.plugin.jrimum.dsl.BoletoDsl
import com.sysdata.gestaofrota.proc.cobrancaBancaria.BancoCobranca
import grails.util.Holders
import org.jrimum.bopepo.view.BoletoViewer
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa
import org.jrimum.domkee.financeiro.banco.febraban.Agencia
import org.jrimum.domkee.financeiro.banco.febraban.Carteira
import org.jrimum.domkee.financeiro.banco.febraban.Cedente
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta
import org.jrimum.domkee.financeiro.banco.febraban.Sacado
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo
import org.jrimum.domkee.financeiro.banco.febraban.Titulo

class Fatura {

    Conta conta
    Corte corte
    Date data
    Date dataVencimento
    StatusFatura status

    def grailsApplication

    static hasMany = [itens:ItemFatura,boletos:Boleto]

    static transients = ['valorTotal','grailsApplication']

    static constraints = {
    }

    BigDecimal getValorTotal() {
        this.itens.sum { it.valor }
    }

    String toString() {
        "FAT => #${this.id} cnt:${this.conta.id} vcto:${this.dataVencimento.format('dd/MM/yyyy')} sts:${this.status.nome} total:${Util.formatCurrency(this.valorTotal)}"
    }

    Boleto gerarBoleto(){

        Boleto boleto=new Boleto()
        boleto.status=StatusBoleto.CRIADO

        def nomeCedente=Holders.grailsApplication.config.project.administradora.nome
        def cnpjCedente=Holders.grailsApplication.config.project.administradora.cnpj
        def codigoBanco=Holders.grailsApplication.config.project.administradora.contaBancaria.banco
        def contaCorrente=Holders.grailsApplication.config.project.administradora.contaBancaria.numero
        def contaDv=Holders.grailsApplication.config.project.administradora.contaBancaria.numeroDv
        def cart=Holders.grailsApplication.config.project.administradora.contaBancaria.carteira
        def codigoAgencia=Holders.grailsApplication.config.project.administradora.contaBancaria.agencia
        def agenciaDv=Holders.grailsApplication.config.project.administradora.contaBancaria.agenciaDv

        BancoCobranca bancoCobranca=BancoCobranca.factoryMethod(codigoBanco)

        Rh rh=this.conta.participante as Rh

        def nossoNum=this.id.toString().padLeft(8,"0")
        def dacNossoNumero=bancoCobranca.calcularDacNossoNumero(nossoNum)

        Cedente cedente=new Cedente(nomeCedente,cnpjCedente)
        Sacado sacado=new Sacado(rh.nome,rh.cnpj)

        Endereco domainEndereco=rh.endereco
        if (domainEndereco==null) throw new Exception("Endereço não encontrado")
        org.jrimum.domkee.comum.pessoa.endereco.Endereco endereco = new org.jrimum.domkee.comum.pessoa.endereco.Endereco()
        String siglaUF = domainEndereco.cidade?.estado?.uf?.toUpperCase() ?: domainEndereco.cidade?.estado?.uf?.toUpperCase()
        endereco.setUF(UnidadeFederativa.valueOfSigla(siglaUF))
        endereco.setLocalidade(domainEndereco.cidade?.nome)
        endereco.setCep(domainEndereco.cep)
        endereco.setBairro(domainEndereco.bairro)
        endereco.setLogradouro(domainEndereco.logradouro)
        endereco.setNumero(domainEndereco.numero)
        sacado.addEndereco(endereco)

        //Dados de Conta Bancária
        ContaBancaria contaBancaria=new ContaBancaria()
        contaBancaria.setBanco(bancoCobranca.banco)
        contaBancaria.setNumeroDaConta(new NumeroDaConta(contaCorrente as int,contaDv))
        contaBancaria.setAgencia(new Agencia(codigoAgencia.toInteger(),agenciaDv))
        contaBancaria.setCarteira(new Carteira(cart.toInteger()))

        Titulo titulo=new Titulo(contaBancaria,sacado,cedente)
        titulo.with {
            dataDoDocumento=new Date().clearTime()
            dataDoVencimento=this.dataVencimento
            numeroDoDocumento=this.id
            nossoNumero=nossoNum
            digitoDoNossoNumero=dacNossoNumero
            valor=this.valorTotal
            tipoDeDocumento=TipoDeTitulo.DM_DUPLICATA_MERCANTIL
        }

        org.jrimum.bopepo.Boleto bolJr=new org.jrimum.bopepo.Boleto(titulo)
        bancoCobranca.adicionarExtensoes(bolJr,titulo)

        bolJr.setLocalPagamento(Holders.grailsApplication.config.project.administradora.contaBancaria.boleto.localPagamento)
        bolJr.setInstrucao1(Holders.grailsApplication.config.project.administradora.contaBancaria.boleto.instrucao1)
        bolJr.setInstrucao2(Holders.grailsApplication.config.project.administradora.contaBancaria.boleto.instrucao2)

        BoletoViewer boletoViewer=new BoletoViewer(bolJr)
        boleto.titulo= bolJr.titulo.numeroDoDocumento
        boleto.linhaDigitavel=bolJr.linhaDigitavel.write()
        boleto.imagem=boletoViewer.pdfAsByteArray
        boleto.nossoNumero=nossoNum+dacNossoNumero
        boleto.dataVencimento=this.dataVencimento

        def filename="/home/acception/tmp/boleto_${bolJr.titulo.numeroDoDocumento}.pdf"
        def file=new File(filename)

        file.withDataOutputStream {o->
            o.write(boletoViewer.pdfAsByteArray)
        }

        addToBoletos(boleto)
        boleto
    }


}
