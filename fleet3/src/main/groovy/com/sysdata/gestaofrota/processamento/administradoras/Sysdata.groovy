package com.sysdata.gestaofrota.processamento.administradoras

import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.ParametroSistema
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.Util
import grails.util.Holders

/**
 * Created by hyago on 06/10/17.
 */
class Sysdata extends AdministradoraCartao {

    Sysdata() {
        super()
    }

    @Override
    String gerarNumero(Administradora administradora, Portador portador) {
        def tipoProg = Holders.grailsApplication.config.projeto.tipoPrograma
        def parceiro = Holders.grailsApplication.config.projeto.parceiro
        def cdRh = portador.unidade.rh.id
        def prov = sprintf("%6d%1d%1d%03d%07d", this.bin.toInteger(), tipoProg, parceiro, cdRh, ++administradora.qtdCartoes)
        administradora.save(flush: true)
        def check = calcularDV(prov)
        prov + check
    }

    @Override
    String gerarSenha() {
        Random random = new Random()
        int tamanho = (int) (Math.pow(10, Util.DIGITOS_SENHA) - 1)
        int senha = random.nextInt(tamanho)
        String fmt = "%0" + Util.DIGITOS_SENHA + "d"

        String.format(fmt, senha)
    }

    @Override
    Date gerarDataValidade() {
        Calendar cal = Calendar.getInstance()
        cal.setTime(new Date())
        Integer anosValidade = ParametroSistema.getValorAsInteger(ParametroSistema.ANOS_VALIDADE_CARTAO)
        if (! anosValidade)
            throw new RuntimeException("Parâmetro Anos de Validade Cartão não definido!")
        cal.add(Calendar.YEAR, anosValidade)

        cal.getTime()
    }

    @Override
    String calcularDV(String numero) {
        int valor;
        int soma = 0;
        int multiplicador = 1;

        for (int i = 0; i < numero.length(); i++) {
            valor = Integer.parseInt(numero.substring(i, i + 1)) * multiplicador
            soma = soma + (valor / 10) + (valor % 10)

            multiplicador = multiplicador == 1 ? 2 : 1
        }

        String.valueOf((10 - (soma % 10)) % 10)
    }
}
