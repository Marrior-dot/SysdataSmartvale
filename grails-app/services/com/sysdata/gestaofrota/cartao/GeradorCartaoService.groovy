package com.sysdata.gestaofrota.cartao

import com.sysdata.gestaofrota.Funcionario
import com.sysdata.gestaofrota.Util

abstract class GeradorCartaoService {


    private String calcularDV(String card) {
        int valor;
        int soma = 0;
        int multiplicador = 1;
        int tamanho = card.length();
        for (int i = 0; i < tamanho; i++) {
            valor = Integer.parseInt(card.substring(i, i + 1)) * multiplicador;
            soma = soma + (valor / 10) + (valor % 10);
            if (multiplicador == 1)
                multiplicador = 2;
            else
                multiplicador = 1;
        }
        return String.valueOf((10 - (soma % 10)) % 10);
    }


    String gerarSenha() {
        Random random = new Random()
        int tamanho = (int) (Math.pow(10, Util.DIGITOS_SENHA) - 1)
        int senha = random.nextInt(tamanho)
        String fmt = "%0" + Util.DIGITOS_SENHA + "d"
        String.format(fmt, senha)
    }


    abstract String gerarNumero(Funcionario funcionario)

}