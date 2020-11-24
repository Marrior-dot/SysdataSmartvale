package com.sysdata.gestaofrota

class MensalAgendaPedido extends AgendaPedido {

    Integer dia
    String finalizaEm

    static constraints = {

        finalizaEm validator: { obj, val ->

            def matcher = val =~ /^(?<mes>\d{2})\/(?<ano>\d{4})$/
            if (matcher.matches()) {
                def mes = matcher.group("mes") as int
                def ano = matcher.group("ano") as int
                if (mes < 1 || mes > 12)
                    return "mensalagendapedido.finalizaem.mesinvalido"

                def anoAtual = new Date()[Calendar.YEAR]
                if (ano < anoAtual)
                    return "mensalagendapedido.finalizaem.anoinvalido"

            }

        }
    }
}
