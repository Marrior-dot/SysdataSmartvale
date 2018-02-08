package com.sysdata.gestaofrota

/**
 * Created by acception on 07/02/18.
 */
enum StatusCorte {

    ABERTO("Aberto"),
    FECHADO("Fechado"),
    CANCELADO("Cancelado")

    String nome

    StatusCorte(nome){
        this.nome=nome
    }
}