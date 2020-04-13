package com.sysdata.gestaofrota

/**
 * Created by acception on 16/02/18.
 */
enum StatusFatura {

    ABERTA("Aberta"),
    FECHADA("Fechada")

    String nome

    StatusFatura(nome){
        this.nome=nome
    }
}