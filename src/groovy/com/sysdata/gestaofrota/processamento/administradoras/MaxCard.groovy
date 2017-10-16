package com.sysdata.gestaofrota.processamento.administradoras

import com.sysdata.gestaofrota.ParametroSistema
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.Util

/**
 * Created by hyago on 06/10/17.
 */
class MaxCard extends AdministradoraCartao {

    MaxCard() {
        super()
    }

    @Override
    String gerarNumero(Portador portador) {
        int qtde = portador.unidade.rh.qtdeContas

        final String pp = "05"
        final String nnnnnnn = (++qtde).toString().padLeft(7, '0')

        portador.unidade.rh.qtdeContas = qtde
        portador.unidade.rh.save()

        String numero = "${getBin()}${pp}${nnnnnnn}"

        numero + calcularDV(numero)
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
        cal.add(Calendar.YEAR, anosValidade)

        cal.getTime()
    }

    @Override
    String calcularDV(String numero) {
        return calculaDVBase11(numero)
    }
}
