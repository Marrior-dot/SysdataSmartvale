package com.sysdata.gestaofrota

import com.mrkonno.plugin.jrimum.dsl.BoletoDsl
import com.sysdata.gestaofrota.proc.cobrancaBancaria.BancoCobranca
import grails.util.Holders
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa
import org.jrimum.domkee.financeiro.banco.febraban.Cedente
import org.jrimum.domkee.financeiro.banco.febraban.Sacado
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo

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


    private org.jrimum.domkee.comum.pessoa.endereco.Endereco montarEndereco(participante){
        Endereco domainEndereco = participante.endereco
        if (domainEndereco == null) throw new Exception("Endereço não encontrado")
        org.jrimum.domkee.comum.pessoa.endereco.Endereco endereco = new org.jrimum.domkee.comum.pessoa.endereco.Endereco()
        String siglaUF = domainEndereco.cidade?.estado?.uf?.toUpperCase() ?: domainEndereco.cidade?.estado?.uf?.toUpperCase()
        endereco.setUF(UnidadeFederativa.valueOfSigla(siglaUF))
        endereco.setLocalidade(domainEndereco.cidade?.nome)
        endereco.setCep(domainEndereco.cep)
        endereco.setBairro(domainEndereco.bairro)
        endereco.setLogradouro(domainEndereco.logradouro)
        endereco.setNumero(domainEndereco.numero)
        return endereco
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

        def nossoNumero=this.id.toString().padLeft(7,"0")

        def dacNossoNumero=bancoCobranca.calcularDacNossoNumero(nossoNumero)

        def boletoDsl=BoletoDsl.boleto{

            sacado(rh.nome,rh.cnpj){
                enderecos{montarEndereco(rh)}
            }
            cedente(nomeCedente,cnpjCedente){}

            contaBancaria{
                banco bancoCobranca.banco
                agencia codigoAgencia,agenciaDv
                conta contaCorrente,contaDv
                carteira cart
            }

            dataVencimento this.dataVencimento
            numeroDocumento this.id
            nossoNumero nossoNumero
            digitoNossoNumero dacNossoNumero
            valor this.valorTotal
            tipoTitulo TipoDeTitulo.DM_DUPLICATA_MERCANTIL
            localPagamento "Pagável em qualquer Banco"
            instrucoes """Aceitar ate a data de vencimento
Apos o vencimento aceito apenas nas agencias do Banco do Brasil
Cobrar multa de 7% e juros
"""

        }

        boleto.linhaDigitavel=boletoDsl.boleto.linhaDigitavel.write
        boleto.nossoNumero=nossoNumero+dacNossoNumero
        boleto.imagem=boletoDsl.bytes
        this.addToBoletos(boleto)
        boleto

    }


}
