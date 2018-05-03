package com.sysdata.gestaofrota

import com.mrkonno.plugin.jrimum.dsl.BoletoDsl
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


    private int calcularDAC(String num,multip=2){
        def soma=0
        for(int i=num.length();i>=0;i--){
            multip=multip==0?2:multip-1
            def prod=num[i]*multip
            prod=(prod%10==0)?prod:(int)(prod/10)+prod%10
            soma+=prod
        }
        return 10-(soma%10)
    }


    Boleto gerarBoleto(){
        def docCedente
        def docSacado


        Administradora adm=Administradora.all[0]
        Rh rh=this.conta.participante as Rh

        Cedente cedente=new Cedente(adm.nome,docCedente)
        Sacado sacado=new Sacado(rh.nome,docSacado)
        sacado.addEndereco(montarEndereco(rh))


        def boletoDsl=BoletoDsl.boleto{

            sacado(rh.nome,rh.cnpj){}
            cedente(grailsApplication.config.project.administradora.nome,grailsApplication.config.project.administradora.cnpj){}

            contaBancaria{
                banco grailsApplication.config.project.administradora.contaBancaria.banco
                conta grailsApplication.config.project.administradora.contaBancaria.conta,grailsApplication.config.project.administradora.contaBancaria.contadv
                carteira grailsApplication.config.project.administradora.contaBancaria.carteira
                agencia grailsApplication.config.project.administradora.contaBancaria.agencia,grailsApplication.config.project.administradora.contaBancaria.agenciadv
            }

            dataVencimento this.dataVencimento
            numeroDocumento this.id
            nossoNumero
            digitoNossoNumero
            valor this.valorTotal
            tipoTitulo TipoDeTitulo.DM_DUPLICATA_MERCANTIL
            localPagamento "Pagável em qualquer Banco"
            instrucoes """Aceitar ate a data de vencimento
Apos o vencimento aceito apenas nas agencias do Banco do Brasil
Cobrar multa de 7% e juros
"""

        }


    }


}
