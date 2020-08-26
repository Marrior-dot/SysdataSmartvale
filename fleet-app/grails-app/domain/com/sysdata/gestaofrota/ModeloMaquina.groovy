package com.sysdata.gestaofrota

abstract class ModeloMaquina {
    String nome
    String abreviacao

    static constraints = {
        nome nullable: false, blank: false
        abreviacao nullable: false, blank: false, maxSize: 6
    }

/*    void setNome(String nome) {
        this.nome = nome.toUpperCase()
    }
    void setAbreviacao(String abreviacao) {
        this.abreviacao = abreviacao.toUpperCase()
    }*/

    @Override
    String toString() {
        return nome
    }
}