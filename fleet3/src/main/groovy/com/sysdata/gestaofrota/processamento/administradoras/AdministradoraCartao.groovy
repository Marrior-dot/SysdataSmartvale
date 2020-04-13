package com.sysdata.gestaofrota.processamento.administradoras

import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.processamento.IGeradorCartao

/**
 * Created by hyago on 06/10/17.
 */
abstract class AdministradoraCartao implements IGeradorCartao {
    private String m_Bin

    String getBin() {
        return m_Bin
    }

    AdministradoraCartao() {
        this.m_Bin = Administradora.list().first().bin
    }


    @Override
    abstract String gerarNumero(Administradora administradora, Portador portador)

    @Override
    String gerarSenha(){}

    @Override
    abstract Date gerarDataValidade()

    @Override
    abstract String calcularDV(String numero)

    @Override
    String gerarCVV(){
        final int count = 3
        final Random r = new Random()
        String cvv = ""
        count.times {
            cvv += r.nextInt(10).toString()
        }


        cvv.substring(0, 3)
    }

// Módulo 10
    // Conforme o esquema abaixo, cada dígito do número, começando da direita para a esquerda
    // (menos significativo para o mais significativo) é multiplicado, na ordem, por 2, depois 1, depois 2, depois 1 e
    // assim sucessivamente.
    // Em vez de ser feito o somatório das multiplicações, será feito o somatório dos dígitos das multiplicações
    // (se uma multiplicação der 12, por exemplo, será somado 1 + 2 = 3).
    // O somatório será dividido por 10 e se o resto (módulo 10) for diferente de zero, o dígito será 10 menos este valor.
    // Número exemplo: 261533-4
    //  +---+---+---+---+---+---+   +---+
    //  | 2 | 6 | 1 | 5 | 3 | 3 | - | 4 |
    //  +---+---+---+---+---+---+   +---+
    //    |   |   |   |   |   |
    //   x1  x2  x1  x2  x1  x2
    //    |   |   |   |   |   |
    //   =2 =12  =1 =10  =3  =6
    //    +---+---+---+---+---+-> = (16 / 10) = 1, resto 6 => DV = (10 - 6) = 4
    public static String calculaDVBase10(String numero) {
        int soma = 0;
        int resto = 0;
        int dv = 0;
        String[] numeros = new String[numero.length() + 1];
        int multiplicador = 2;
        String aux;
        String aux2;
        String aux3;
        for (int i = numero.length(); i > 0; i--) {
            //Multiplica da direita pra esquerda, alternando os algarismos 2 e 1
            if (multiplicador % 2 == 0) {
                // pega cada numero isoladamente
                numeros[i] = String.valueOf(Integer.valueOf(numero.substring(i - 1, i)) * 2);
                multiplicador = 1;
            } else {
                numeros[i] = String.valueOf(Integer.valueOf(numero.substring(i - 1, i)) * 1);
                multiplicador = 2;
            }
        }
        // Realiza a soma dos defCampos de acordo com a regra
        for (int i = (numeros.length - 1); i > 0; i--) {
            aux = String.valueOf(Integer.valueOf(numeros[i]));
            if (aux.length() > 1) {
                aux2 = aux.substring(0, aux.length() - 1);
                aux3 = aux.substring(aux.length() - 1, aux.length());
                numeros[i] = String.valueOf(Integer.valueOf(aux2) + Integer.valueOf(aux3));
            } else {
                numeros[i] = aux;
            }
        }
        //Realiza a soma de todos os elementos do array e calcula o digito verificador
        //na base 10 de acordo com a regra.
        for (int i = numeros.length; i > 0; i--) {
            if (numeros[i - 1] != null) {
                soma += Integer.valueOf(numeros[i - 1]);
            }
        }
        resto = soma % 10;
        dv = 10 - resto;
        //retorna o digito verificador
        return dv;
    }

    // Módulo 11
    // Conforme o esquema abaixo, para calcular o primeiro dígito verificador, cada dígito do número,
    // começando da direita para a esquerda (do dígito menos significativo para o dígito mais significativo)
    // é multiplicado, na ordem, por 2, depois 3, depois 4 e assim sucessivamente, até o primeiro dígito do número.
    // O somatório dessas multiplicações dividido por 11. O resto desta divisão (módulo 11) é subtraido da base (11),
    // o resultado é o dígito verificador. Para calcular o próximo dígito, considera-se o dígito anterior como parte
    // do número e efetua-se o mesmo processo. No exemplo, foi considerado o número 261533:
    //  +---+---+---+---+---+---+   +---+
    //  | 2 | 6 | 1 | 5 | 3 | 3 | - | 9 |
    //  +---+---+---+---+---+---+   +---+
    //    |   |   |   |   |   |
    //   x7  x6  x5  x4  x3  x2
    //    |   |   |   |   |   |
    //   =14 =36  =5 =20  =9  =6 soma = 90
    //    +---+---+---+---+---+-> = (90 / 11) = 8,1818 , resto 2 => DV = (11 - 2) = 9
    public static String calculaDVBase11(String numero) {
        int soma = 0;
        int resto = 0;
        int dv = 0;
        int multiplicador = 2;

        String[] numeros = new String[numero.length() + 1];
        for (int i = numero.length(); i > 0; i--) {
            //Multiplica da direita pra esquerda, incrementando o multiplicador de 2 a 9
            //Caso o multiplicador seja maior que 9 o mesmo recomeça em 2
            if (multiplicador > 9) {
                // pega cada numero isoladamente
                multiplicador = 2;
                numeros[i] = String.valueOf(Integer.valueOf(numero.substring(i - 1, i)) * multiplicador);
                multiplicador++;
            } else {
                numeros[i] = String.valueOf(Integer.valueOf(numero.substring(i - 1, i)) * multiplicador);
                multiplicador++;
            }
        }
        //Realiza a soma de todos os elementos do array e calcula o digito verificador
        //na base 11 de acordo com a regra.
        for (int i = numeros.length; i > 0; i--) {
            if (numeros[i - 1] != null) {
                soma += Integer.valueOf(numeros[i - 1]);
            }
        }
        resto = soma % 11;
        dv = 11 - resto;
        //retorna o digito verificador
        if(dv > 9 || dv == 0 )
            dv = 1
        return dv;
    }
}
