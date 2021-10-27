package com.sysdata.gestaofrota

class Processadora extends Participante {

    static hasMany = [role: Role]

    static constraints = {
    }

    private static Cidade cidade

    static Cidade getCidade() {
        if (! cidade)
            cidade = Cidade.findWhere(nome: "BELÉM", estado: Estado.findWhere(uf: 'PA'))
        return cidade
    }

}
