package com.sysdata.gestaofrota.proc.cartao

import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.ParametroSistema
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.Util

class GeradorCartaoPadrao implements IGeradorCartao {

    private Random randomSenha = new Random()
    private Random randomCvv = new Random()

    private def spec16d = [
        [name: "bin", size: 6, value: { it.adm.bin }],
        [name: "projeto", size: 1, value: "1" ],
        [name: "sequencia", size: 8, value: { it.adm.qtdCartoes + 1 } ],
        [name: "dv", size: 1, value: { calcularDV(it.numero)} ],
    ]

    @Override
    String gerarNumero(Administradora administradora, Portador portador) {
        def numero = new StringBuilder()
        this.spec16d.each { f ->
            String value
            if (f.value instanceof Closure)
                value = (f.value.call([adm: administradora, port: portador, numero: numero.toString()]))
            else
                value = f.value
            if (value.length() <= f.size)
                value = sprintf("%${f.size < 10 ? '0' + f.size : f.size}d", value as long)
            else
                throw new RuntimeException("Valor campo $f.name maior (${value.length()}) do que o definido ($f.size)")
            numero.append(value)
        }
        numero.toString()
    }

    @Override
    String gerarSenha() {
        int tamanho = (int) (Math.pow(10, Util.DIGITOS_SENHA) - 1)
        int senha = randomSenha.nextInt(tamanho)
        sprintf("%0" + Util.DIGITOS_SENHA + "d", senha)
    }

    @Override
    Date gerarDataValidade() {
        Calendar cal = Calendar.getInstance()
        cal.setTime(new Date().clearTime())
        Integer anosValidade = ParametroSistema.getValorAsInteger(ParametroSistema.ANOS_VALIDADE_CARTAO)
        if (! anosValidade)
            throw new RuntimeException("Parâmetro Anos de Validade Cartão não definido!")
        cal.add(Calendar.YEAR, anosValidade)
        cal.getTime()
    }

    @Override
    String gerarCVV() {
        int tamanho = (int) (Math.pow(10, Util.DIGITOS_CVV) - 1)
        int cvv = randomCvv.nextInt(tamanho)
        sprintf("%0" + Util.DIGITOS_CVV + "d", cvv)
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