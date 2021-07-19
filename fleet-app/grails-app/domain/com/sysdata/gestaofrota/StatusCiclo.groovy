package com.sysdata.gestaofrota

/**
 * Created by luiz on 18/07/16.
 */
enum StatusCiclo {

    ABERTO("Aberto"),
    FECHADO("Fechado")

    String nome

    StatusCiclo(nome){
        this.nome=nome
    }
}