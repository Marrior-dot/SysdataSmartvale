package com.sysdata.gestaofrota.proc.faturamento.boleto

import com.sysdata.gestaofrota.Boleto
import com.sysdata.gestaofrota.Conta
import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.ItemFatura
import com.sysdata.gestaofrota.Lancamento
import com.sysdata.gestaofrota.Rh
import grails.testing.mixin.integration.Integration
import grails.transaction.*
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Integration
@Rollback
class GeradorBoletoBancoBrasilServiceSpec extends Specification {

    @Autowired GeradorBoletoBancoBrasilService geradorBoletoBancoBrasilService

    def setup() {
    }

    def cleanup() {
    }

    void "testar Criar Empresa Cliente"() {

        when:
            Rh rh = new Rh()
            rh.nome = "Empresa Teste I"
            rh.save(flush: true)

            def hoje = new Date().clearTime()
            Fatura fatura = new Fatura(conta: rh.conta, data: hoje, dataVencimento: hoje + 5)
            fatura.addToItens(new ItemFatura(valor: 10.00))
            fatura.save(flush: true)

            Boleto boleto = new Boleto(fatura: fatura, dataVencimento: fatura.dataVencimento)
            boleto.valor = fatura.valorTotal
            boleto.save(flush: true)

            geradorBoletoBancoBrasilService.gerarBoleto(boleto)

        then:

            def file = new File("/home/luiz/frota/bahiavale/BancoDoBrasil.pdf")
            file.exists()

            def df = new Date().clearTime()
            df.set([dayOfMonth: 4, month: 9, year: 2020])

            fatura.valorTotal == 10.00 && fatura.dataVencimento == df && boleto.dataVencimento == df && boleto.valor == 10.00 && file.exists()


    }

/*
    void "testar geração boleto"() {

        when:

            Conta conta = new Conta()
            conta.save(flush: true)

            Rh rh = new Rh()
            rh.conta = conta
            rh.save(flush: true)

            def hoje = new Date().clearTime()
            Fatura fatura = new Fatura(conta: rh.conta, data: hoje, dataVencimento: hoje + 5)
            fatura.addToItens(new ItemFatura(valor: 10.00))
            fatura.save(flush: true)


            Boleto boleto = new Boleto(fatura: fatura, dataVencimento: fatura.dataVencimento)
            boleto.valor = fatura.valorTotal
            boleto.save(flush: true)

            geradorBoletoBancoBrasilService.gerarBoleto(boleto)
        then:

            def file = new File("/home/luiz/frota/bahiavale/BancoDoBrasil.pdf")
            file.exists()
    }
*/
}
